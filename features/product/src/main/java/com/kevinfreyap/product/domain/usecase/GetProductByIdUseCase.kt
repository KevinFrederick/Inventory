package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.product.domain.model.Product
import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.repository.IProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val repository: IProductRepository
) {
    operator fun invoke(productId: ProductId): Flow<Product?> {
        return repository.getProductById(productId)
    }
}