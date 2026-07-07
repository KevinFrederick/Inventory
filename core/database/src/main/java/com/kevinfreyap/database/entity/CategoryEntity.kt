package com.kevinfreyap.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey (autoGenerate = false)
    val categoryId: String,
    val name: String,
    val description: String?,
    val createdAt: Long,
    val lastUpdated: Long
)
