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

    @Query("SELECT * FROM location WHERE syncState != 'DELETED'")
    fun getAllLocation(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM location WHERE locationId = :id AND syncState !='DELETED'")
    fun getLocationById(id: String): Flow<LocationEntity>

    @Query("SELECT * FROM location WHERE name = :name AND syncState !='DELETED' LIMIT 1")
    suspend fun getLocationByName(name: String): LocationEntity?

    @Update
    suspend fun updateLocation(location: LocationEntity)

    @Query("DELETE FROM location WHERE locationId = :id")
    suspend fun deleteLocation(id: String)

    // Sync
    @Upsert
    suspend fun upsertAll(locations: List<LocationEntity>)

    @Query("DELETE FROM location WHERE locationId IN (:ids)")
    suspend fun deleteByIds(ids: List<String>)

    @Query("SELECT * FROM location WHERE syncState != 'SYNCED'")
    suspend fun getUnsyncedLocations(): List<LocationEntity>

    @Query("DELETE FROM location WHERE locationId IN (:ids) AND syncState = 'DELETED'")
    suspend fun clearTombstones(ids: List<String>)

    @Query("UPDATE location SET syncState = 'SYNCED' WHERE locationId IN (:ids)")
    suspend fun markAsSynced(ids: List<String>)

    @Query("UPDATE location SET syncState = :state WHERE locationId = :id")
    suspend fun markAsDeleted(id: String, state: SyncState)
}