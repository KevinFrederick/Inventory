package com.kevinfreyap.network.dto.response

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class GroupResponse(
    val groupId: String,
    val name: String,
    val description: String?,
    val address: String?,
    val createdAt: Long,
    val lastUpdated: Long
)
