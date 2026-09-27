package com.kevinfreyap.product.data.network.dto.response

import kotlinx.serialization.Serializable

@Serializable
data class ImageUploadResponse(
    val imageUrl: String
)
