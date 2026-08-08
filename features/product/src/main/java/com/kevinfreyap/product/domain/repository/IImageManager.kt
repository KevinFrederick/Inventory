package com.kevinfreyap.product.domain.repository

interface IImageManager {
    suspend fun getFileSizeInMb(uriString: String): Double
    suspend fun saveImageToInternalStorage(uriString: String): String?
    suspend fun deleteImage (imagePath: String): Boolean
}