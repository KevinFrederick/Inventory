package com.kevinfreyap.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class LocationEntity(
    @PrimaryKey(autoGenerate = false)
    val locationId: String,
    val name: String,
    val description: String?,
    val locationBarcode: String?,
    val createdAt: Long,
    val lastUpdated: Long
)
