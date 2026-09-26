package com.kevinfreyap.product.data.repository

import androidx.room.withTransaction
import com.kevinfreyap.database.AppDatabase
import com.kevinfreyap.database.dao.BatchDao
import com.kevinfreyap.database.dao.ProductDao
import com.kevinfreyap.database.model.SyncState
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
        timestamp: Long
    ) {
        database.withTransaction {
            batchDao.insertBatch(stockBatch.toEntity(SyncState.CREATED))
            productDao.updateProductTimestamp(
                productId = stockBatch.productId.value,
                timestamp = timestamp
            )
        }
    }

    override fun getBatchById(id: BatchId): Flow<StockBatchDetails?> {
        return batchDao.getBatchById(id.value).map { batchWithLocation ->
            batchWithLocation?.toDomain()
        }
    }

    override fun getInventorySummary(): Flow<InventorySummary> {
        return batchDao.getInventorySummary()
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
        timestamp: Long
    ): Int {
        val oldEntity = batchDao.getBatchSnapshot(stockBatch.batchId.value) ?: return 0

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
                    timestamp = timestamp
                )
            }

            rowUpdated
        }
    }

    override suspend fun deleteBatch(
        batchId: BatchId,
        productId: ProductId,
        timestamp: Long
    ) {
        val oldEntity = batchDao.getBatchSnapshot(batchId.value) ?: return

        if (oldEntity.batch.syncState == SyncState.CREATED) {
            database.withTransaction {
                batchDao.deleteBatch(batchId.value)
                productDao.updateProductTimestamp(
                    productId = productId.value,
                    timestamp = timestamp
                )
            }
        } else {
            database.withTransaction {
                batchDao.markAsDeleted(batchId.value, SyncState.DELETED)
                productDao.updateProductTimestamp(
                    productId = productId.value,
                    timestamp = timestamp
                )
            }
        }

    }

    override suspend fun deleteBatchesForProduct(productId: ProductId) {
        database.withTransaction {
            val batches = batchDao.getBatchesByProductId(productId.value)

            batches.forEach { batch ->
                if (batch.batch.syncState == SyncState.CREATED) {
                    batchDao.deleteBatch(batch.batch.batchId)
                } else {
                    batchDao.markAsDeleted(batch.batch.batchId, SyncState.DELETED)
                }
            }
        }
    }
}