package com.kevinfreyap.user.data.network.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class GroupMemberResponse(
    val userId: String,
    val avatarUrl: String?,
    val name: String,
    val email: String,
    val role: String,
    val joinedAt: Long,
)
