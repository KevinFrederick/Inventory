package com.kevinfreyap.product.domain.repository

import androidx.paging.PagingData
import com.kevinfreyap.product.domain.model.Product
import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.model.query.ProductQueryFilter
import kotlinx.coroutines.flow.Flow

interface IProductRepository {
    suspend fun insertProduct(product: Product)

    fun getAllProduct(filterQuery: ProductQueryFilter): Flow<PagingData<Product>>

    fun getProductCount(): Flow<Int>

    fun getRecentProduct(limit: Int = 3): Flow<List<Product>>

    fun getLowStockProduct(): Flow<List<Product>>

    fun getProductById(productId: ProductId): Flow<Product?>

    suspend fun updateProduct(product: Product)

    suspend fun deleteProduct(productId: ProductId)
}