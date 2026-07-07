package com.kevinfreyap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kevinfreyap.database.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Query("SELECT * FROM category")
    fun getAllCategory(): Flow<List<CategoryEntity>>

    @Query("DELETE FROM category WHERE categoryId = :id")
    suspend fun deleteCategory(id: String)
}