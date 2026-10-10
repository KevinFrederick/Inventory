package com.kevinfreyap.auth.data.network.dto.response

import com.kevinfreyap.network.dto.response.UserGroupResponse
import com.kevinfreyap.network.dto.response.UserResponse
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: UserResponse,
    val groups: List<UserGroupResponse>
)
