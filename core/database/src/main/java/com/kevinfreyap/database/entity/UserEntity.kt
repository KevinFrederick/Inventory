package com.kevinfreyap.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("user")
data class UserEntity(
    @PrimaryKey
    val userId: String,
    val email: String,
    val name: String,
    val profilePictureUrl: String?,
    val phoneNumber: String?,
    val jobTitle: String?,
    val locale: String?,
    val timeZone: String?,
    val createdAt: Long,
    val lastUpdated: Long
)
