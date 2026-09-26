package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.product.domain.manager.ISyncManager
import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.repository.IProductRepository
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val repository: IProductRepository,
    private val syncManager: ISyncManager
) {
    suspend operator fun invoke(productId: ProductId) {
        repository.deleteProduct(productId)
        syncManager.triggerSync()
    }
}