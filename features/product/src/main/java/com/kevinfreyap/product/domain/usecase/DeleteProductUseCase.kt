package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.product.domain.manager.ISyncManager
import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.repository.IProductRepository
import com.kevinfreyap.product.domain.repository.IStockBatchRepository
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val productRepository: IProductRepository,
    private val batchRepository: IStockBatchRepository,
    private val syncManager: ISyncManager
) {
    suspend operator fun invoke(productId: ProductId) {
        productRepository.deleteProduct(productId)
        batchRepository.deleteBatchesForProduct(productId)
        syncManager.triggerSync()
    }
}