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

    @Query("SELECT * FROM category WHERE groupId = :groupId AND syncState != 'DELETED'")
    fun getAllCategory(groupId: String): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM category WHERE name = :name AND groupId = :groupId AND syncState != 'DELETED' LIMIT 1")
    suspend fun getCategoryByName(name: String, groupId: String): CategoryEntity?

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Query("DELETE FROM category WHERE categoryId = :id AND groupId = :groupId")
    suspend fun deleteCategory(id: String, groupId: String)

    // Sync
    @Upsert
    suspend fun upsertAll(categories: List<CategoryEntity>)

    @Query("DELETE FROM category WHERE categoryId IN (:ids) AND groupId = :groupId")
    suspend fun deleteByIds(ids: List<String>, groupId: String)

    @Query("SELECT * FROM category WHERE syncState != 'SYNCED' AND groupId = :groupId")
    suspend fun getUnsyncedCategories(groupId: String): List<CategoryEntity>

    @Query("DELETE FROM category WHERE categoryId IN (:ids) AND syncState = 'DELETED' AND groupId = :groupId")
    suspend fun clearTombstones(ids: List<String>, groupId: String)

    @Query("UPDATE category SET syncState = 'SYNCED' WHERE categoryId IN (:ids) AND groupId = :groupId")
    suspend fun markAsSynced(ids: List<String>, groupId: String)

    @Query("UPDATE category SET syncState = :state WHERE categoryId = :id AND groupId = :groupId")
    suspend fun markAsDeleted(id: String, state: SyncState, groupId: String)
}