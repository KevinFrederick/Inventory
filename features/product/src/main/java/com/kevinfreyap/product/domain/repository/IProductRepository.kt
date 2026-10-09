package com.kevinfreyap.product.domain.repository

import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.product.domain.model.FilteredPagingStream
import com.kevinfreyap.product.domain.model.Product
import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.model.query.ProductQueryFilter
import kotlinx.coroutines.flow.Flow

interface IProductRepository {
    suspend fun insertProduct(product: Product)

    suspend fun isSkuDuplicate(sku: String, groupId: GroupId): Boolean

    suspend fun isBarcodeDuplicate(barcode: String, groupId: GroupId): Boolean

    fun getProductStream(filterProvider: () -> ProductQueryFilter, groupId: GroupId): FilteredPagingStream<Product>

    fun getDynamicProductCount(filter: ProductQueryFilter, groupId: GroupId): Flow<Int>

    fun getRecentProduct(limit: Int = 3, groupId: GroupId): Flow<List<Product>>

    fun getLowStockProduct(groupId: GroupId): Flow<List<Product>>

    fun getProductById(productId: ProductId, groupId: GroupId): Flow<Product?>

    suspend fun getProductByBarcode(barcode: String, groupId: GroupId): Product?

    suspend fun updateProduct(product: Product, groupId: GroupId)

    suspend fun deleteProduct(productId: ProductId, groupId: GroupId)
}