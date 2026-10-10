package com.kevinfreyap.user.data.network.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateMemberRoleRequest(
    val role: String,
)
