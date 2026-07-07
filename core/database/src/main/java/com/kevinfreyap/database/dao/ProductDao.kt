package com.kevinfreyap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kevinfreyap.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Query("SELECT * FROM product")
    fun getAllProduct(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM product WHERE productId = :id")
    fun getProduct(id: String): Flow<ProductEntity>

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("DELETE FROM product WHERE productId = :id")
    suspend fun deleteProduct(id: String)
}