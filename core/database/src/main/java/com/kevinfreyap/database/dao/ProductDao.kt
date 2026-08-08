package com.kevinfreyap.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.kevinfreyap.database.entity.CategoryEntity
import com.kevinfreyap.database.entity.LocationEntity
import com.kevinfreyap.database.entity.ProductEntity
import com.kevinfreyap.database.entity.StockBatchEntity
import com.kevinfreyap.database.entity.relation.ProductWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM product WHERE sku = :sku)")
    suspend fun isSkuDuplicate(sku: String): Boolean

    @Transaction
    @RawQuery(observedEntities = [
        ProductEntity::class,
        CategoryEntity::class,
        LocationEntity::class,
        StockBatchEntity::class
    ])
    fun getAllProduct(query: SupportSQLiteQuery): PagingSource<Int, ProductWithDetails>

    @Transaction
    @RawQuery(observedEntities = [
        ProductEntity::class,
        CategoryEntity::class,
        LocationEntity::class,
        StockBatchEntity::class
    ])
    fun getDynamicProductCount(query: SupportSQLiteQuery): Flow<Int>

    @Transaction
    @Query("SELECT * FROM product ORDER BY lastUpdated DESC LIMIT :limit")
    fun getRecentProduct(limit: Int): Flow<List<ProductWithDetails>>

    @Transaction
    @Query("""
        SELECT p.*
        FROM product as p
        LEFT JOIN stock_batch as b ON p.productId = b.productId
        GROUP BY p.productId
        HAVING COALESCE(SUM(b.quantity), 0) <= p.minimumQuantity
        ORDER BY 
            COALESCE(SUM(b.quantity), 0) ASC,
            p.createdAt DESC
    """)
    fun getLowStockProducts(): Flow<List<ProductWithDetails>>

    @Transaction
    @Query("SELECT * FROM product WHERE productId = :id")
    fun getProduct(id: String): Flow<ProductWithDetails>

    @Query("SELECT COUNT(*) FROM product")
    fun getProductCount(): Flow<Int>

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("DELETE FROM product WHERE productId = :id")
    suspend fun deleteProduct(id: String)
}