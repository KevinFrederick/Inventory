package com.kevinfreyap.product.data.manager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.net.toUri
import com.kevinfreyap.product.domain.manager.IImageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

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

                val mimeType = context.contentResolver.getType(imageUri)
                val isPng = mimeType == "image/png"

                val extension = if (isPng) "png" else "jpg"
                val filename = "product_img_${System.currentTimeMillis()}.$extension"
                val permanentFile = File(context.filesDir, filename)

                var rotationDegrees = 0f
                context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                    val exif = ExifInterface(inputStream)
                    val orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL
                    )
                    rotationDegrees = when (orientation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> 90f
                        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
                        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
                        else -> 0f
                    }
                }

                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }
                context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream, null, options)
                }

                options.inSampleSize = calculateInSampleSize(options, 1024, 1024)
                options.inJustDecodeBounds = false

                val bitmap = context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                } ?: return@withContext uriString

                val finalBitmap = if (rotationDegrees != 0f) {
                    val matrix = Matrix().apply { postRotate(rotationDegrees) }
                    Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                } else {
                    bitmap
                }

                FileOutputStream(permanentFile).use { outputStream ->
                    if (isPng) {
                        finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    } else {
                        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
                    }
                }

                if (bitmap != finalBitmap) bitmap.recycle()
                finalBitmap.recycle()

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

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.outHeight to options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}