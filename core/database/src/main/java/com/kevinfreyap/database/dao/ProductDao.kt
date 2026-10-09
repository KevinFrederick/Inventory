package com.kevinfreyap.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapColumn
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
import com.kevinfreyap.database.model.ProductImagePath
import com.kevinfreyap.database.model.SyncState
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM product WHERE sku = :sku AND syncState != 'DELETED' AND groupId = :groupId) ")
    suspend fun isSkuDuplicate(sku: String, groupId: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM product WHERE barcode = :barcode AND syncState != 'DELETED' AND groupId = :groupId)")
    suspend fun isBarcodeDuplicate(barcode: String, groupId: String): Boolean

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
    @Query("SELECT * FROM product WHERE syncState != 'DELETED' AND groupId = :groupId ORDER BY lastUpdated DESC LIMIT :limit")
    fun getRecentProduct(limit: Int, groupId: String): Flow<List<ProductWithDetails>>

    @Transaction
    @Query("""
        SELECT p.*
        FROM product as p
        LEFT JOIN stock_batch as b ON p.productId = b.productId
        WHERE p.syncState != 'DELETED' AND p.groupId = :groupId
        GROUP BY p.productId
        HAVING COALESCE(SUM(b.quantity), 0) <= p.minimumQuantity
        ORDER BY 
            COALESCE(SUM(b.quantity), 0) ASC,
            p.createdAt DESC
    """)
    fun getLowStockProducts(groupId: String): Flow<List<ProductWithDetails>>

    @Transaction
    @Query("SELECT * FROM product WHERE productId = :id AND syncState != 'DELETED' AND groupId = :groupId")
    fun getProduct(id: String, groupId: String): Flow<ProductWithDetails?>

    @Transaction
    @Query("SELECT * FROM product WHERE productId = :id AND groupId = :groupId LIMIT 1")
    suspend fun getProductSnapshot(id: String, groupId: String): ProductWithDetails?

    @Transaction
    @Query("SELECT * FROM product WHERE barcode = :barcode AND syncState != 'DELETED' AND groupId = :groupId LIMIT 1")
    suspend fun getProductByBarcode(barcode: String, groupId: String): ProductWithDetails?

    @Query("SELECT groupId, COUNT(*) as productCount FROM product WHERE groupId IN (:groupIds) AND syncState != 'DELETED' GROUP BY groupId")
    fun getProductCount(groupIds: List<String>): Flow<Map<@MapColumn(columnName= "groupId") String, @MapColumn(columnName = "productCount") Int >>

    @Query("""
        SELECT * FROM product
        WHERE localImagePath IS NOT NULL
        AND remoteImageUrl IS NULL
        AND syncState != 'DELETED'
        AND groupId = :groupId
    """)
    suspend fun getProductWithUnsyncedImage(groupId: String): List<ProductEntity>

    @Query("SELECT productId, localImagePath FROM product WHERE productId IN (:ids) AND groupId = :groupId")
    suspend fun getLocalImagePaths(ids: List<String>, groupId: String): List<ProductImagePath>

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("UPDATE product SET remoteImageUrl = :remoteUrl WHERE productId = :productId AND groupId = :groupId")
    suspend fun updateRemoteImageUrl(productId: String, remoteUrl: String, groupId: String)

    @Query("UPDATE product SET lastUpdated = :timestamp WHERE productId = :productId AND groupId = :groupId")
    suspend fun updateProductTimestamp(productId: String, timestamp: Long, groupId: String)

    @Query("DELETE FROM product WHERE productId = :id AND groupId = :groupId")
    suspend fun deleteProduct(id: String, groupId: String)

    // Sync
    @Upsert
    suspend fun upsertAll(products: List<ProductEntity>)

    @Query("DELETE FROM product WHERE productId IN (:ids) AND groupId = :groupId")
    suspend fun deleteByIds(ids: List<String>, groupId: String)

    @Query("SELECT * FROM product WHERE syncState != 'SYNCED' AND groupId = :groupId")
    suspend fun getUnsyncedProducts(groupId: String): List<ProductEntity>

    @Query("DELETE FROM product WHERE productId IN (:ids) AND syncState = 'DELETED' AND groupId = :groupId")
    suspend fun clearTombstones(ids: List<String>, groupId: String)

    @Query("UPDATE product SET syncState = 'SYNCED' WHERE productId IN (:ids) AND groupId = :groupId")
    suspend fun markAsSynced(ids: List<String>, groupId: String)

    @Query("UPDATE product SET syncState = :state WHERE productId = :id AND groupId = :groupId")
    suspend fun markAsDeleted(id: String, state: SyncState, groupId: String)
}