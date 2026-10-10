package com.kevinfreyap.product.domain.repository

import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.product.domain.model.BatchId
import com.kevinfreyap.product.domain.model.InventorySummary
import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.model.StockBatch
import com.kevinfreyap.product.domain.model.StockBatchDetails
import kotlinx.coroutines.flow.Flow

interface IStockBatchRepository {
    suspend fun insertBatchToProduct(stockBatch: StockBatch, timestamp: Long, groupId: GroupId)

    fun getBatchById(id: BatchId, groupId: GroupId): Flow<StockBatchDetails?>

    fun getInventorySummary(groupId: GroupId): Flow<InventorySummary>

    suspend fun updateBatch(stockBatch: StockBatch, timestamp: Long, groupId: GroupId): Int

    suspend fun deleteBatch(batchId: BatchId, productId: ProductId, timestamp: Long, groupId: GroupId)

    suspend fun deleteBatchesForProduct(productId: ProductId, groupId: GroupId)
}