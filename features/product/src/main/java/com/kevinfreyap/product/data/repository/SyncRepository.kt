package com.kevinfreyap.product.data.repository

import android.util.Log
import androidx.room.withTransaction
import com.kevinfreyap.database.AppDatabase
import com.kevinfreyap.database.datastore.SyncPreferences
import com.kevinfreyap.database.model.SyncState
import com.kevinfreyap.domain.Result
import com.kevinfreyap.product.data.mapper.toEntity
import com.kevinfreyap.product.data.mapper.toRequest
import com.kevinfreyap.product.data.network.dto.sync.SyncPayloadDto
import com.kevinfreyap.product.data.network.source.SyncRemoteDataSource
import com.kevinfreyap.network.error.NetworkError
import com.kevinfreyap.product.domain.repository.ISyncRepository
import java.io.File
import javax.inject.Inject

class SyncRepository @Inject constructor(
    private val remoteDataSource: SyncRemoteDataSource,
    private val db: AppDatabase,
    private val preference: SyncPreferences
): ISyncRepository {
    override suspend fun sync(): Result<Unit, NetworkError> {
        return try {
            val imageResult = syncPendingImages()
            if (imageResult is Result.Error) return imageResult

            val pushResult = pushLocalChanges()
            if (pushResult is Result.Error) return pushResult

            val pullResult = pullRemoteChanges()
            if (pullResult is Result.Error) return pullResult

            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e("SyncRepository", "Sync failed with exception: ${e.message}")
            Result.Error(NetworkError.Local.UNKNOWN)
        }
    }

    override suspend fun observeRealTimeUpdates() {
        remoteDataSource.observeSyncPings().collect {
            sync()
        }
    }

    private suspend fun pushLocalChanges(): Result<Unit, NetworkError> {
        val unsyncedCategory = db.categoryDao().getUnsyncedCategories()
        val unsyncedLocation = db.locationDao().getUnsyncedLocations()
        val unsyncedProducts = db.productDao().getUnsyncedProducts()
        val unsyncedBatches = db.batchDao().getUnsyncedBatches()

        if (unsyncedCategory.isEmpty() &&
            unsyncedLocation.isEmpty() &&
            unsyncedProducts.isEmpty() &&
            unsyncedBatches.isEmpty()){
            return Result.Success(Unit)
        }

        val payload = SyncPayloadDto(
            createdCategories = unsyncedCategory.filter { it.syncState == SyncState.CREATED }.map { it.toRequest() },
            updatedCategories = unsyncedCategory.filter { it.syncState == SyncState.UPDATED }.map { it.toRequest() },
            deletedCategories = unsyncedCategory.filter { it.syncState == SyncState.DELETED }.map { it.categoryId },

            createdLocations = unsyncedLocation.filter { it.syncState == SyncState.CREATED }.map { it.toRequest() },
            updatedLocations = unsyncedLocation.filter { it.syncState == SyncState.UPDATED }.map { it.toRequest() },
            deletedLocations = unsyncedLocation.filter { it.syncState == SyncState.DELETED }.map { it.locationId },

            createdProduct = unsyncedProducts.filter { it.syncState == SyncState.CREATED }.map { it.toRequest() },
            updatedProduct = unsyncedProducts.filter { it.syncState == SyncState.UPDATED }.map { it.toRequest() },
            deletedProduct = unsyncedProducts.filter { it.syncState == SyncState.DELETED }.map { it.productId },

            createdBatches = unsyncedBatches.filter { it.syncState == SyncState.CREATED }.map { it.toRequest() },
            updatedBatches = unsyncedBatches.filter { it.syncState == SyncState.UPDATED }.map { it.toRequest() },
            deletedBatches = unsyncedBatches.filter { it.syncState == SyncState.DELETED }.map { it.batchId }
        )

        return when(
            val response = remoteDataSource.pushSync(payload)
        ) {
            is Result.Success -> {
                db.withTransaction {
                    db.batchDao().clearTombstones(payload.deletedBatches)
                    db.productDao().clearTombstones(payload.deletedProduct)
                    db.categoryDao().clearTombstones(payload.deletedCategories)
                    db.locationDao().clearTombstones(payload.deletedLocations)

                    db.categoryDao().markAsSynced(
                        payload.createdCategories.map { it.categoryId } +
                        payload.updatedCategories.map { it.categoryId }
                    )
                    db.locationDao().markAsSynced(
                        payload.createdLocations.map { it.locationId } +
                        payload.updatedLocations.map { it.locationId }
                    )
                    db.productDao().markAsSynced(
                        payload.createdProduct.map { it.productId } +
                        payload.updatedProduct.map { it.productId }
                    )
                    db.batchDao().markAsSynced(
                        payload.createdBatches.map { it.batchId } +
                        payload.updatedBatches.map { it.batchId }
                    )
                }
                Result.Success(Unit)
            }
            is Result.Error -> response
        }
    }

    private suspend fun pullRemoteChanges(): Result<Unit, NetworkError> {
        val lastSync = preference.getLastSyncTimestamp()

        return when(
            val response = remoteDataSource.pullSync(lastSync)
        ) {
            is Result.Success -> {
                val data = response.data

                db.withTransaction {
                    db.batchDao().deleteByIds(data.deletedBatches)
                    db.productDao().deleteByIds(data.deletedProducts)
                    db.categoryDao().deleteByIds(data.deletedCategories)
                    db.locationDao().deleteByIds(data.deletedLocations)

                    db.categoryDao().upsertAll(data.categories.map { it.toEntity() })
                    db.locationDao().upsertAll(data.locations.map { it.toEntity() })

                    val incomingIds = data.products.map { it.productId }

                    val localPathMap = db.productDao().getLocalImagePaths(incomingIds)
                        .associateBy({it.productId}, {it.localImagePath})

                    val productEntities = data.products.map { dto ->
                        dto.toEntity().copy(
                            localImagePath = localPathMap[dto.productId]
                        )
                    }

                    db.productDao().upsertAll(productEntities)
                    db.batchDao().upsertAll(data.batches.map { it.toEntity() })
                }

                preference.saveLastSyncTimestamp(data.serverTimeStamp)
                Result.Success(Unit)
            }
            is Result.Error -> response
        }
    }

    private suspend fun syncPendingImages(): Result<Unit, NetworkError> {
        val productsWithImages = db.productDao().getProductWithUnsyncedImage()

        if (productsWithImages.isEmpty()) return Result.Success(Unit)

        for (product in productsWithImages) {
            val localPath = product.localImagePath ?: continue
            val file = File(localPath)

            if (!file.exists()) {
                Log.e("SyncRepository", "Local file not found for product: ${product.productId}")
            }

            when(
                val uploadResult = remoteDataSource.uploadImage(product.productId, file)
            ) {
                is Result.Success -> {
                    val remoteUrl = uploadResult.data
                    db.productDao().updateRemoteImageUrl(product.productId, remoteUrl)
                }
                is Result.Error -> return uploadResult
            }
        }

        return Result.Success(Unit)
    }
}