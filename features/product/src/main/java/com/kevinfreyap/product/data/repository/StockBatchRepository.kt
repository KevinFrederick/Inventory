package com.kevinfreyap.product.data.repository

import com.kevinfreyap.database.dao.BatchDao
import com.kevinfreyap.product.data.mapper.toDomain
import com.kevinfreyap.product.data.mapper.toEntity
import com.kevinfreyap.product.domain.model.BatchId
import com.kevinfreyap.product.domain.model.StockBatch
import com.kevinfreyap.product.domain.model.StockBatchDetails
import com.kevinfreyap.product.domain.repository.IStockBatchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StockBatchRepository @Inject constructor(
    private val batchDao: BatchDao
): IStockBatchRepository {
    override suspend fun insertBatchToProduct(stockBatch: StockBatch) {
        batchDao.insertBatch(stockBatch.toEntity())
    }

    override fun getBatchById(id: BatchId): Flow<StockBatchDetails?> {
        return batchDao.getBatchById(id.value).map { batchWithLocation ->
            batchWithLocation?.toDomain()
        }
    }

    override suspend fun updateBatch(stockBatch: StockBatch): Int {
        return batchDao.updateBatch(stockBatch.toEntity())
    }

    override suspend fun deleteBatch(id: BatchId) {
        batchDao.deleteBatch(id.value)
    }
}