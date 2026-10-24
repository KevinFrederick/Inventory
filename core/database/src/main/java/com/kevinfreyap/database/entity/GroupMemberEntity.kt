package com.kevinfreyap.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "group_members",
    primaryKeys = ["groupId", "userId"],
    foreignKeys = [
        ForeignKey(
            entity = GroupEntity::class,
            parentColumns = ["groupId"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GroupMemberEntity(
    val groupId: String,
    val userId: String,
    val avatarUrl: String?,
    val name: String,
    val email: String,
    val role: String,
    val status: String,
    val joinedAt: Long
)
