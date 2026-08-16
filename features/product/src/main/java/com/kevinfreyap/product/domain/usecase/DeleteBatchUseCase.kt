package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.product.domain.model.BatchId
import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.repository.IStockBatchRepository
import javax.inject.Inject

class DeleteBatchUseCase @Inject constructor(
    private val repository: IStockBatchRepository
) {
    suspend operator fun invoke(
        batchId: BatchId,
        productId: ProductId
    ) {
        repository.deleteBatch(
            batchId = batchId,
            productId = productId,
            timestamp = System.currentTimeMillis()
        )
    }
}