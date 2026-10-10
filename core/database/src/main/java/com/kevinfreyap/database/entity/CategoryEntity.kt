package com.kevinfreyap.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kevinfreyap.database.model.SyncState

@Entity(
    tableName = "category",
    foreignKeys = [
        ForeignKey(
            entity = GroupEntity::class,
            parentColumns = ["groupId"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["groupId", "categoryId"], unique = true),
        Index(value = ["name"], unique = true)
    ]
)
data class CategoryEntity(
    @PrimaryKey (autoGenerate = false)
    val categoryId: String,
    val groupId: String,
    val name: String,
    val description: String?,
    val createdAt: Long,
    val lastUpdated: Long,
    val syncState: SyncState = SyncState.SYNCED
)
