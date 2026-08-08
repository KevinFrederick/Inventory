package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.product.domain.model.query.ProductQueryFilter
import com.kevinfreyap.product.domain.repository.IProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilteredProductCountUseCase @Inject constructor(
    private val repository: IProductRepository
) {
    operator fun invoke(filter: ProductQueryFilter): Flow<Int> {
        return repository.getDynamicProductCount(filter)
    }
}