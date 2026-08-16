package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.product.domain.model.BatchId
import com.kevinfreyap.product.domain.model.StockBatch
import com.kevinfreyap.product.domain.model.StockBatchDetails
import com.kevinfreyap.product.domain.repository.IStockBatchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBatchByIdUseCase @Inject constructor(
    private val repository: IStockBatchRepository
) {
    operator fun invoke(batchId: BatchId): Flow<StockBatchDetails?> {
        return repository.getBatchById(batchId)
    }
}