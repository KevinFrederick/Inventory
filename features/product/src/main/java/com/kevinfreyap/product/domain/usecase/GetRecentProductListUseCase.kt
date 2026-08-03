package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.product.domain.model.Product
import com.kevinfreyap.product.domain.repository.IProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentProductListUseCase @Inject constructor(
    private val repository: IProductRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return repository.getRecentProduct()
    }
}