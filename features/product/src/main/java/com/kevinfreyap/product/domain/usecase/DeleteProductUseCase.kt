package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.repository.IProductRepository
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val repository: IProductRepository
) {
    suspend operator fun invoke(productId: ProductId) {
        repository.deleteProduct(productId)
    }
}