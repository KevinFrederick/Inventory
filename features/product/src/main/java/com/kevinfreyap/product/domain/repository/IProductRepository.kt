package com.kevinfreyap.product.domain.repository

import androidx.paging.PagingSource
import com.kevinfreyap.product.domain.model.FilteredPagingStream
import com.kevinfreyap.product.domain.model.Product
import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.model.query.ProductQueryFilter
import kotlinx.coroutines.flow.Flow

interface IProductRepository {
    suspend fun insertProduct(product: Product)

    suspend fun isSkuDuplicate(sku: String): Boolean

    fun getProductStream(filterProvider: () -> ProductQueryFilter): FilteredPagingStream<Product>

    fun getProductCount(): Flow<Int>

    fun getRecentProduct(limit: Int = 3): Flow<List<Product>>

    fun getLowStockProduct(): Flow<List<Product>>

    fun getProductById(productId: ProductId): Flow<Product?>

    suspend fun updateProduct(product: Product)

    suspend fun deleteProduct(productId: ProductId)
}