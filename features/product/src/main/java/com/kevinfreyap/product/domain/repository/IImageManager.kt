package com.kevinfreyap.product.domain.repository

interface IImageManager {
    suspend fun getFileSizeInMb(uriString: String): Double
}