package com.kevinfreyap.product.domain.usecase

import androidx.paging.PagingData
import com.kevinfreyap.product.domain.model.Product
import com.kevinfreyap.product.domain.model.query.ProductQueryFilter
import com.kevinfreyap.product.domain.repository.IProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilteredProductUseCase @Inject constructor (
    private val repository: IProductRepository
) {
    operator fun invoke(filter: ProductQueryFilter): Flow<PagingData<Product>> {
        return repository.getAllProduct(filter)
    }
}