package com.kevinfreyap.product.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import com.kevinfreyap.database.AppDatabase
import com.kevinfreyap.database.dao.BatchDao
import com.kevinfreyap.database.dao.ProductDao
import com.kevinfreyap.database.query.ProductDbFilter
import com.kevinfreyap.database.query.ProductQueryBuilder
import com.kevinfreyap.product.data.mapper.toDomain
import com.kevinfreyap.product.data.mapper.toDomainList
import com.kevinfreyap.product.data.mapper.toEntity
import com.kevinfreyap.product.domain.model.Product
import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.model.query.FilterDateOption
import com.kevinfreyap.product.domain.model.query.ProductQueryFilter
import com.kevinfreyap.product.domain.repository.IProductRepository
import com.kevinfreyap.product.domain.util.DateCalculator.calculateDateRange
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

    override fun getAllProduct(filterQuery: ProductQueryFilter): Flow<PagingData<Product>> {
        val calculatedDate = calculateDateRange(filterQuery.filterDateOption)

        val startDate = if (filterQuery.filterDateOption == FilterDateOption.PICK_DATE) {
            filterQuery.startDate
        } else {
            calculatedDate.startMillis
        }

        val endDate = if (filterQuery.filterDateOption == FilterDateOption.PICK_DATE) {
            filterQuery.endDate
        } else {
            calculatedDate.endMillis
        }

        val dbFilter = ProductDbFilter (
            searchQuery = filterQuery.searchQuery,
            sortBy = filterQuery.sortConfig.option.columnName,
            sortDirection = filterQuery.sortConfig.direction.sqlString,
            categoryList = filterQuery.categoryList?.map { it.value },
            locationId = filterQuery.locationId?.value,
            startDate = startDate,
            endDate = endDate
        )

        val query = ProductQueryBuilder().build(dbFilter)

        return Pager(
            config = PagingConfig(
                pageSize = 50,
            ),
            pagingSourceFactory = {
                productDao.getAllProduct(query)
            }
        ).flow.map { pagingData ->
            pagingData.map { productWithDetails ->
                productWithDetails.toDomain()
            }
        }
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
        TODO("Not yet implemented")
    }

    override suspend fun updateProduct(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProduct(productId: ProductId) {
        TODO("Not yet implemented")
    }

}