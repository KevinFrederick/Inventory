package com.kevinfreyap.auth.data.network.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class LogoutRequest(
    val refreshToken: String
)
