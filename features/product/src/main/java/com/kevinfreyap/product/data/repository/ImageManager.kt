package com.kevinfreyap.product.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.kevinfreyap.product.domain.repository.IImageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import androidx.core.net.toUri

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