package com.kevinfreyap.product.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.kevinfreyap.product.domain.repository.IImageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileOutputStream

class ImageManager @Inject constructor (
    @param:ApplicationContext private val context: Context
): IImageManager {
    override suspend fun getFileSizeInMb(uriString: String): Double {
        return try {
            val uri = uriString.toUri()

            val sizeInBytes = when(uri.scheme) {
                "content" -> getSizeFromContentUri(uri)
                "file" -> getSizeFromFileUri(uri)
                else -> 0L
            }

            // Convert to MB
            sizeInBytes / (1024.0 * 1024.0)
        } catch (e: Exception) {
            e.printStackTrace()

            Double.MAX_VALUE
        }
    }

    override suspend fun saveImageToInternalStorage(uriString: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val imageUri = uriString.toUri()
                val inputStream = context.contentResolver.openInputStream(imageUri) ?: return@withContext null

                val filename = "product_img_${System.currentTimeMillis()}.jpg"
                val permanentFile = File(context.filesDir, filename)

                inputStream.use { input ->
                    FileOutputStream(permanentFile).use { outputStream ->
                        input.copyTo(outputStream)
                    }
                }

                permanentFile.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    override suspend fun deleteImage(imagePath: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(imagePath)
                if (file.exists()) file.delete() else true
            } catch (_: Exception) {
                false
            }
        }
    }

    private fun getSizeFromContentUri(uri: Uri): Long {
        var size = 0L

        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (sizeIndex != -1) {
                    size = cursor.getLong(sizeIndex)
                }
            }
        }
        return size
    }

    private fun getSizeFromFileUri(uri: Uri): Long {
        val path = uri.path ?: return 0L
        val file = File(path)
        return if (file.exists()) file.length() else 0L
    }
}