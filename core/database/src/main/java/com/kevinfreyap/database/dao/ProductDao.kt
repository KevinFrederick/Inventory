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

    @Transaction
    @RawQuery(observedEntities = [
        ProductEntity::class,
        CategoryEntity::class,
        LocationEntity::class,
        StockBatchEntity::class
    ])
    fun getAllProduct(query: SupportSQLiteQuery): PagingSource<Int, ProductWithDetails>

    @Transaction
    @Query("SELECT * FROM product ORDER BY createdAt DESC LIMIT :qty")
    fun getRecentProduct(qty: Int): Flow<List<ProductWithDetails>>

    @Transaction
    @Query("SELECT * FROM product WHERE productId = :id")
    fun getProduct(id: String): Flow<ProductWithDetails>

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("DELETE FROM product WHERE productId = :id")
    suspend fun deleteProduct(id: String)
}