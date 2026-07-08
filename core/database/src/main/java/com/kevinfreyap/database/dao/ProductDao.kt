package com.kevinfreyap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.kevinfreyap.database.entity.ProductEntity
import com.kevinfreyap.database.entity.relation.ProductWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Transaction
    @Query("SELECT * FROM product")
    fun getAllProduct(): Flow<List<ProductWithCategory>>

    @Transaction
    @Query("SELECT * FROM product WHERE productId = :id")
    fun getProduct(id: String): Flow<ProductWithCategory>

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("DELETE FROM product WHERE productId = :id")
    suspend fun deleteProduct(id: String)
}