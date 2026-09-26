package com.kevinfreyap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.kevinfreyap.database.entity.CategoryEntity
import com.kevinfreyap.database.model.SyncState
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: CategoryEntity)

    @Query("SELECT * FROM category WHERE syncState != 'DELETED'")
    fun getAllCategory(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM category WHERE name = :name AND syncState != 'DELETED' LIMIT 1")
    suspend fun getCategoryByName(name: String): CategoryEntity?

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Query("DELETE FROM category WHERE categoryId = :id")
    suspend fun deleteCategory(id: String)

    // Sync
    @Upsert
    suspend fun upsertAll(categories: List<CategoryEntity>)

    @Query("DELETE FROM category WHERE categoryId IN (:ids)")
    suspend fun deleteByIds(ids: List<String>)

    @Query("SELECT * FROM category WHERE syncState != 'SYNCED'")
    suspend fun getUnsyncedCategories(): List<CategoryEntity>

    @Query("DELETE FROM category WHERE categoryId IN (:ids) AND syncState = 'DELETED'")
    suspend fun clearTombstones(ids: List<String>)

    @Query("UPDATE category SET syncState = 'SYNCED' WHERE categoryId IN (:ids)")
    suspend fun markAsSynced(ids: List<String>)

    @Query("UPDATE category SET syncState = :state WHERE categoryId = :id")
    suspend fun markAsDeleted(id: String, state: SyncState)
}