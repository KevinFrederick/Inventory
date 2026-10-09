package com.kevinfreyap.domain.model

@JvmInline
value class UserId(val value: String)

data class User(
    val userId: UserId,
    val email: String,
    val name: String,
    val profilePictureUrl: String?,
    val phoneNumber: String?,
    val jobTitle: String?,
    val locale: String?,
    val timeZone: String?,
    val createdAt: Long,
    val lastUpdated: Long,
)
