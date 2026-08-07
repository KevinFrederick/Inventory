package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.product.domain.model.FilteredPagingStream
import com.kevinfreyap.product.domain.model.Product
import com.kevinfreyap.product.domain.model.query.ProductQueryFilter
import com.kevinfreyap.product.domain.repository.IProductRepository
import javax.inject.Inject

class GetFilteredProductUseCase @Inject constructor (
    private val repository: IProductRepository
) {
    operator fun invoke(filterProvider: () -> ProductQueryFilter): FilteredPagingStream<Product> {
        return repository.getProductStream(filterProvider)
    }
}