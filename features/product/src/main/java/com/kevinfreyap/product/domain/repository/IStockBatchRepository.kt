package com.kevinfreyap.product.domain.repository

import com.kevinfreyap.product.domain.model.BatchId
import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.model.StockBatch
import com.kevinfreyap.product.domain.model.StockBatchDetails
import kotlinx.coroutines.flow.Flow

interface IStockBatchRepository {
    suspend fun insertBatchToProduct(stockBatch: StockBatch, timestamp: Long)

    fun getBatchById(id: BatchId): Flow<StockBatchDetails?>

    suspend fun updateBatch(stockBatch: StockBatch, timestamp: Long): Int

    suspend fun deleteBatch(batchId: BatchId, productId: ProductId, timestamp: Long)
}