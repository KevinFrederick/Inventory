package com.kevinfreyap.product.data.repository

import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import androidx.room.withTransaction
import com.kevinfreyap.database.AppDatabase
import com.kevinfreyap.database.dao.BatchDao
import com.kevinfreyap.database.dao.ProductDao
import com.kevinfreyap.database.query.ProductQueryBuilder
import com.kevinfreyap.product.data.mapper.toDbFilter
import com.kevinfreyap.product.data.mapper.toDomain
import com.kevinfreyap.product.data.mapper.toDomainList
import com.kevinfreyap.product.data.mapper.toEntity
import com.kevinfreyap.product.domain.model.FilteredPagingStream
import com.kevinfreyap.product.domain.model.Product
import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.model.query.ProductQueryFilter
import com.kevinfreyap.product.domain.repository.IProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val database: AppDatabase,
    private val productDao: ProductDao,
    private val batchDao: BatchDao
): IProductRepository {
    override suspend fun insertProduct(product: Product) {
        val productEntity = product.toEntity()
        val batchEntities = product.batches.map { it.toEntity() }

        database.withTransaction {
            productDao.insertProduct(productEntity)
            batchEntities.forEach { batchEntity ->
                batchDao.insertBatch(batchEntity)
            }
        }
    }

    override suspend fun isSkuDuplicate(sku: String): Boolean {
        return productDao.isSkuDuplicate(sku)
    }

    override fun getProductStream(
        filterProvider: () -> ProductQueryFilter
    ): FilteredPagingStream<Product> {
        val pagingSourceFactory = InvalidatingPagingSourceFactory {
            val dbFilter = filterProvider().toDbFilter()
            val query = ProductQueryBuilder().build(dbFilter)

            productDao.getAllProduct(query)
        }

        // create pager and map to domain model
        val pagerFlow = Pager(
            config = PagingConfig(
                pageSize = 50
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }

        return FilteredPagingStream(
            flow = pagerFlow,
            invalidate = {
                pagingSourceFactory.invalidate()
            }
        )
    }

    override fun getDynamicProductCount(filter: ProductQueryFilter): Flow<Int> {
        val dbFilter = filter.toDbFilter()
        val query = ProductQueryBuilder().build(dbFilter, isCountQuery = true)

        return productDao.getDynamicProductCount(query)
    }

    override fun getProductCount(): Flow<Int> {
        return productDao.getProductCount()
    }

    override fun getRecentProduct(limit: Int): Flow<List<Product>> {
        return productDao.getRecentProduct(limit).map { productWithDetails ->
            productWithDetails.toDomainList()
        }
    }

    override fun getLowStockProduct(): Flow<List<Product>> {
        return productDao.getLowStockProducts().map { productWithDetails ->
            productWithDetails.toDomainList()
        }
    }

    override fun getProductById(productId: ProductId): Flow<Product?> {
        return productDao.getProduct(productId.value).map { productWithDetails ->
            productWithDetails?.toDomain()
        }
    }

    override suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product.toEntity())
    }

    override suspend fun deleteProduct(productId: ProductId) {
        productDao.deleteProduct(productId.value)
    }

}