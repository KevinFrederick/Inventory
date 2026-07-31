package com.kevinfreyap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kevinfreyap.database.entity.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationEntity)

    @Query("SELECT * FROM location")
    fun getAllLocation(): Flow<List<LocationEntity>>

    @Query("SELECT * FROM location WHERE locationId = :id")
    fun getLocation(id: String): Flow<LocationEntity>

    @Update
    suspend fun updateLocation(location: LocationEntity)

    @Query("DELETE FROM location WHERE locationId = :id")
    suspend fun deleteLocation(id: String)
}