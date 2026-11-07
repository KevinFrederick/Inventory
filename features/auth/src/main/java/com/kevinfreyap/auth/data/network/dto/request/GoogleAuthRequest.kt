package com.kevinfreyap.auth.data.network.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GoogleAuthRequest(
    @SerialName("id_token")
    val idToken: String
)
