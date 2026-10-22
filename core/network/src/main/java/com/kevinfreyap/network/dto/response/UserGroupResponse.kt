package com.kevinfreyap.network.dto.response

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class UserGroupResponse(
    val group: GroupResponse,
    val role: String,
    val status: String,
    val joinedAt: Long,
)
