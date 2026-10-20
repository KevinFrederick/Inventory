package com.kevinfreyap.user.data.network.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse(
    val message: String,
)
