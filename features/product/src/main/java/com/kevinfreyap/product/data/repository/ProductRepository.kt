package com.kevinfreyap.product.data.repository

import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import androidx.room.withTransaction
import com.kevinfreyap.database.AppDatabase
import com.kevinfreyap.database.dao.BatchDao
import com.kevinfreyap.database.dao.ProductDao
import com.kevinfreyap.database.model.SyncState
import com.kevinfreyap.database.query.ProductQueryBuilder
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.repository.IProductStatsRepository
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
): IProductRepository, IProductStatsRepository {
    override suspend fun insertProduct(product: Product) {
        val productEntity = product.toEntity(syncState = SyncState.CREATED)
        val batchEntities = product.batches.map { it.toEntity(syncState = SyncState.CREATED) }

        database.withTransaction {
            productDao.insertProduct(productEntity)
            batchEntities.forEach { batchEntity ->
                batchDao.insertBatch(batchEntity)
            }
        }
    }

    override suspend fun isSkuDuplicate(sku: String, groupId: GroupId): Boolean {
        return productDao.isSkuDuplicate(sku, groupId.value)
    }

    override suspend fun isBarcodeDuplicate(barcode: String, groupId: GroupId): Boolean {
        return productDao.isBarcodeDuplicate(barcode, groupId.value)
    }

    override fun getProductStream(
        filterProvider: () -> ProductQueryFilter,
        groupId: GroupId
    ): FilteredPagingStream<Product> {
        val pagingSourceFactory = InvalidatingPagingSourceFactory {
            val dbFilter = filterProvider().toDbFilter()
            val query = ProductQueryBuilder().build(
                groupId = groupId.value,
                filter = dbFilter,
            )

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

    override fun getDynamicProductCount(filter: ProductQueryFilter, groupId: GroupId): Flow<Int> {
        val dbFilter = filter.toDbFilter()
        val query = ProductQueryBuilder().build(
            groupId = groupId.value,
            filter = dbFilter,
            isCountQuery = true
        )

        return productDao.getDynamicProductCount(query)
    }

    override fun observeProductCount(groupIds: List<GroupId>): Flow<Map<String, Int>> {
        return productDao.getProductCount(groupIds.map { it.value })
    }

    override fun getRecentProduct(limit: Int, groupId: GroupId): Flow<List<Product>> {
        return productDao.getRecentProduct(limit, groupId.value).map { productWithDetails ->
            productWithDetails.toDomainList()
        }
    }

    override fun getLowStockProduct(groupId: GroupId): Flow<List<Product>> {
        return productDao.getLowStockProducts(groupId.value).map { productWithDetails ->
            productWithDetails.toDomainList()
        }
    }

    override fun getProductById(productId: ProductId, groupId: GroupId): Flow<Product?> {
        return productDao.getProduct(productId.value, groupId.value).map { productWithDetails ->
            productWithDetails?.toDomain()
        }
    }

    override suspend fun getProductByBarcode(barcode: String, groupId: GroupId): Product? {
        return productDao.getProductByBarcode(barcode, groupId.value)?.toDomain()
    }

    override suspend fun updateProduct(product: Product, groupId: GroupId) {
        val oldEntity = productDao.getProductSnapshot(
            id = product.productId.value,
            groupId = groupId.value
        ) ?: return

        val newState = if (oldEntity.product.syncState == SyncState.CREATED) {
            SyncState.CREATED
        } else {
            SyncState.UPDATED
        }

        productDao.updateProduct(product.toEntity(newState))
    }

    override suspend fun deleteProduct(productId: ProductId, groupId: GroupId) {
        val oldEntity = productDao.getProductSnapshot(
            id = productId.value,
            groupId = groupId.value
        ) ?: return

        if (oldEntity.product.syncState == SyncState.CREATED) {
            productDao.deleteProduct(
                id = productId.value,
                groupId = groupId.value
            )
        } else {
            productDao.markAsDeleted(
                id = productId.value,
                state = SyncState.DELETED,

                groupId = groupId.value
            )
        }
    }
}