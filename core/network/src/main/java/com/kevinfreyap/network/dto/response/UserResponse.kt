package com.kevinfreyap.network.dto.response

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class UserResponse(
    val userId: String,
    val name: String,
    val email: String,
    val avatarUrl: String?,
    val phoneNumber: String?,
    val jobTitle: String?,
    val locale: String?,
    val timeZone: String?,
    val isActive: Boolean,
    val createdAt: Long,
    val lastUpdated: Long,
)