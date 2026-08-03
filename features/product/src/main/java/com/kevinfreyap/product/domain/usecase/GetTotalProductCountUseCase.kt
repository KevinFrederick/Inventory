package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.product.domain.repository.IProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTotalProductCountUseCase @Inject constructor(
    private val repository: IProductRepository
) {
    operator fun invoke(): Flow<Int> {
        return repository.getProductCount()
    }
}