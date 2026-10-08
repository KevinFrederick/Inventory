package com.kevinfreyap.network.dto.response

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)