package com.kevinfreyap.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import androidx.sqlite.db.SupportSQLiteQuery
import com.kevinfreyap.database.entity.CategoryEntity
import com.kevinfreyap.database.entity.LocationEntity
import com.kevinfreyap.database.entity.ProductEntity
import com.kevinfreyap.database.entity.StockBatchEntity
import com.kevinfreyap.database.entity.relation.ProductWithDetails
import com.kevinfreyap.database.model.SyncState
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM product WHERE sku = :sku AND syncState != 'DELETED') ")
    suspend fun isSkuDuplicate(sku: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM product WHERE barcode = :barcode AND syncState != 'DELETED')")
    suspend fun isBarcodeDuplicate(barcode: String): Boolean

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
    @Query("SELECT * FROM product WHERE syncState != 'DELETED' ORDER BY lastUpdated DESC LIMIT :limit")
    fun getRecentProduct(limit: Int): Flow<List<ProductWithDetails>>

    @Transaction
    @Query("""
        SELECT p.*
        FROM product as p
        LEFT JOIN stock_batch as b ON p.productId = b.productId
        WHERE p.syncState != 'DELETED'
        GROUP BY p.productId
        HAVING COALESCE(SUM(b.quantity), 0) <= p.minimumQuantity
        ORDER BY 
            COALESCE(SUM(b.quantity), 0) ASC,
            p.createdAt DESC
    """)
    fun getLowStockProducts(): Flow<List<ProductWithDetails>>

    @Transaction
    @Query("SELECT * FROM product WHERE productId = :id AND syncState != 'DELETED'")
    fun getProduct(id: String): Flow<ProductWithDetails?>

    @Transaction
    @Query("SELECT * FROM product WHERE productId = :id LIMIT 1")
    suspend fun getProductSnapshot(id: String): ProductWithDetails?

    @Transaction
    @Query("SELECT * FROM product WHERE barcode = :barcode AND syncState != 'DELETED' LIMIT 1")
    suspend fun getProductByBarcode(barcode: String): ProductWithDetails?

    @Query("SELECT COUNT(*) FROM product WHERE syncState != 'DELETED'")
    fun getProductCount(): Flow<Int>

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("UPDATE product SET lastUpdated = :timestamp WHERE productId = :productId")
    suspend fun updateProductTimestamp(productId: String, timestamp: Long)

    @Query("DELETE FROM product WHERE productId = :id")
    suspend fun deleteProduct(id: String)

    // Sync
    @Upsert
    suspend fun upsertAll(products: List<ProductEntity>)

    @Query("DELETE FROM product WHERE productId IN (:ids)")
    suspend fun deleteByIds(ids: List<String>)

    @Query("SELECT * FROM product WHERE syncState != 'SYNCED'")
    suspend fun getUnsyncedProducts(): List<ProductEntity>

    @Query("DELETE FROM product WHERE productId IN (:ids) AND syncState = 'DELETED'")
    suspend fun clearTombstones(ids: List<String>)

    @Query("UPDATE product SET syncState = 'SYNCED' WHERE productId IN (:ids)")
    suspend fun markAsSynced(ids: List<String>)

    @Query("UPDATE product SET syncState = :state WHERE productId = :id")
    suspend fun markAsDeleted(id: String, state: SyncState)
}