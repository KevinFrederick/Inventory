package com.kevinfreyap.product.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.kevinfreyap.database.dao.ProductDao
import com.kevinfreyap.database.query.ProductDbFilter
import com.kevinfreyap.database.query.ProductQueryBuilder
import com.kevinfreyap.product.data.mapper.toDomain
import com.kevinfreyap.product.data.mapper.toDomainList
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
    private val productDao: ProductDao
): IProductRepository {
    override suspend fun insertProduct(product: Product) {
        TODO("Not yet implemented")
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

    override fun getRecentProduct(limit: Int): Flow<List<Product>> {
        return productDao.getRecentProduct(limit).map { productWithDetails ->
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