package com.kevinfreyap.product.data.repository

import androidx.room.withTransaction
import com.kevinfreyap.database.AppDatabase
import com.kevinfreyap.database.dao.BatchDao
import com.kevinfreyap.database.dao.ProductDao
import com.kevinfreyap.database.model.SyncState
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.product.data.mapper.toDomain
import com.kevinfreyap.product.data.mapper.toEntity
import com.kevinfreyap.product.domain.model.BatchId
import com.kevinfreyap.product.domain.model.InventorySummary
import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.model.StockBatch
import com.kevinfreyap.product.domain.model.StockBatchDetails
import com.kevinfreyap.product.domain.repository.IStockBatchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StockBatchRepository @Inject constructor(
    private val database: AppDatabase,
    private val batchDao: BatchDao,
    private val productDao: ProductDao
): IStockBatchRepository {
    override suspend fun insertBatchToProduct(
        stockBatch: StockBatch,
        timestamp: Long,
        groupId: GroupId
    ) {
        database.withTransaction {
            batchDao.insertBatch(stockBatch.toEntity(SyncState.CREATED))
            productDao.updateProductTimestamp(
                productId = stockBatch.productId.value,
                timestamp = timestamp,
                groupId = groupId.value
            )
        }
    }

    override fun getBatchById(id: BatchId, groupId: GroupId): Flow<StockBatchDetails?> {
        return batchDao.getBatchById(
            id = id.value,
            groupId = groupId.value
        ).map { batchWithLocation ->
            batchWithLocation?.toDomain()
        }
    }

    override fun getInventorySummary(groupId: GroupId): Flow<InventorySummary> {
        return batchDao.getInventorySummary(groupId.value)
            .distinctUntilChanged()
            .map { inventorySummaryDb ->
                InventorySummary(
                    totalProduct = inventorySummaryDb.totalProduct,
                    totalItem = inventorySummaryDb.totalItem,
                    totalValue = inventorySummaryDb.totalValue
                )
            }
    }

    override suspend fun updateBatch(
        stockBatch: StockBatch,
        timestamp: Long,
        groupId: GroupId
    ): Int {
        val oldEntity = batchDao.getBatchSnapshot(
            id = stockBatch.batchId.value,
            groupId = groupId.value
        ) ?: return 0

        val newState = if (oldEntity.batch.syncState == SyncState.CREATED) {
            SyncState.CREATED
        } else {
            SyncState.UPDATED
        }

        return database.withTransaction {
            val rowUpdated = batchDao.updateBatch(stockBatch.toEntity(newState))

            if (rowUpdated > 0) {
                productDao.updateProductTimestamp(
                    productId = stockBatch.productId.value,
                    timestamp = timestamp,
                    groupId = groupId.value
                )
            }

            rowUpdated
        }
    }

    override suspend fun deleteBatch(
        batchId: BatchId,
        productId: ProductId,
        timestamp: Long,
        groupId: GroupId
    ) {
        val oldEntity = batchDao.getBatchSnapshot(
            id = batchId.value,
            groupId = groupId.value
        ) ?: return

        if (oldEntity.batch.syncState == SyncState.CREATED) {
            database.withTransaction {
                batchDao.deleteBatch(
                    id = batchId.value,
                    groupId = groupId.value
                )
                productDao.updateProductTimestamp(
                    productId = productId.value,
                    timestamp = timestamp,
                    groupId = groupId.value
                )
            }
        } else {
            database.withTransaction {
                batchDao.markAsDeleted(
                    id = batchId.value,
                    state = SyncState.DELETED,
                    groupId = groupId.value
                )
                productDao.updateProductTimestamp(
                    productId = productId.value,
                    timestamp = timestamp,
                    groupId = groupId.value
                )
            }
        }

    }

    override suspend fun deleteBatchesForProduct(productId: ProductId, groupId: GroupId) {
        database.withTransaction {
            val batches = batchDao.getBatchesByProductId(
                id = productId.value,
                groupId = groupId.value
            )

            batches.forEach { batch ->
                if (batch.batch.syncState == SyncState.CREATED) {
                    batchDao.deleteBatch(
                        id = batch.batch.batchId,
                        groupId = groupId.value
                    )
                } else {
                    batchDao.markAsDeleted(
                        id = batch.batch.batchId,
                        state = SyncState.DELETED,
                        groupId = groupId.value
                    )
                }
            }
        }
    }
}