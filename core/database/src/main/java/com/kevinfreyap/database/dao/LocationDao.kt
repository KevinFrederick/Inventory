package com.kevinfreyap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.kevinfreyap.database.entity.LocationEntity
import com.kevinfreyap.database.model.SyncState
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLocation(location: LocationEntity)

    @Query("SELECT * FROM location WHERE syncState != 'DELETED' AND groupId = :groupId")
    fun getAllLocation(groupId: String): Flow<List<LocationEntity>>

    @Query("SELECT * FROM location WHERE locationId = :id AND syncState !='DELETED' AND groupId = :groupId")
    fun getLocationById(id: String, groupId: String): Flow<LocationEntity>

    @Query("SELECT * FROM location WHERE name = :name AND syncState !='DELETED' AND groupId = :groupId LIMIT 1")
    suspend fun getLocationByName(name: String, groupId: String): LocationEntity?

    @Update
    suspend fun updateLocation(location: LocationEntity)

    @Query("DELETE FROM location WHERE locationId = :id AND groupId = :groupId")
    suspend fun deleteLocation(id: String, groupId: String)

    // Sync
    @Upsert
    suspend fun upsertAll(locations: List<LocationEntity>)

    @Query("DELETE FROM location WHERE locationId IN (:ids) AND groupId = :groupId")
    suspend fun deleteByIds(ids: List<String>, groupId: String)

    @Query("SELECT * FROM location WHERE syncState != 'SYNCED' AND groupId = :groupId")
    suspend fun getUnsyncedLocations(groupId: String): List<LocationEntity>

    @Query("DELETE FROM location WHERE locationId IN (:ids) AND syncState = 'DELETED' AND groupId = :groupId")
    suspend fun clearTombstones(ids: List<String>, groupId: String)

    @Query("UPDATE location SET syncState = 'SYNCED' WHERE locationId IN (:ids) AND groupId = :groupId")
    suspend fun markAsSynced(ids: List<String>, groupId: String)

    @Query("UPDATE location SET syncState = :state WHERE locationId = :id AND groupId = :groupId")
    suspend fun markAsDeleted(id: String, state: SyncState, groupId: String)
}