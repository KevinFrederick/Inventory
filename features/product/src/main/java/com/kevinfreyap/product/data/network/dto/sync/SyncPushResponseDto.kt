package com.kevinfreyap.product.data.network.dto.sync

import kotlinx.serialization.Serializable

@Serializable
data class SyncPushResponseDto(
    val success: Boolean,
    val message: String? = null,
    val serverTimeStamp: Long,
)
