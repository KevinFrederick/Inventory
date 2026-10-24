package com.kevinfreyap.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("groups")
data class GroupEntity(
    @PrimaryKey
    val groupId: String,
    val name: String,
    val description: String?,
    val address: String?,
    val totalMembers: Int,
    val totalProducts: Int,
    val currentUserRole: String,
    val currentUserStatus: String,
    val createdAt: Long,
    val lastUpdated: Long
)
