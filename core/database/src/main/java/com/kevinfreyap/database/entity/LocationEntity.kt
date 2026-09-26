package com.kevinfreyap.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kevinfreyap.database.model.SyncState

@Entity(
    tableName = "location",
    indices = [
        Index(value = ["name"], unique = true)
    ]
)
data class LocationEntity(
    @PrimaryKey(autoGenerate = false)
    val locationId: String,
    val name: String,
    val description: String?,
    val locationBarcode: String?,
    val createdAt: Long,
    val lastUpdated: Long,
    val syncState: SyncState = SyncState.SYNCED
)
