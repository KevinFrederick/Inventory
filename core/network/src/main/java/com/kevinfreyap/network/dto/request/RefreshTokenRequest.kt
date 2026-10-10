package com.kevinfreyap.network.dto.request

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)