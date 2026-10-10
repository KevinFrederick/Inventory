package com.kevinfreyap.domain.validation

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.error.ImageError
import com.kevinfreyap.domain.manager.IImageManager
import javax.inject.Inject

class ValidateImageUseCase @Inject constructor(
    private val imageManager: IImageManager
) {
    suspend operator fun invoke(imageUri: String?): Result<String?, ImageError> {
        if (imageUri.isNullOrBlank()) return Result.Success(null)

        val sanitizedUri = imageUri.trim()

        val isValidScheme = sanitizedUri.startsWith("content://") ||
                            sanitizedUri.startsWith("file://") ||
                            sanitizedUri.startsWith("http://") ||
                            sanitizedUri.startsWith("https://") ||
                            sanitizedUri.startsWith("/")

        when {
            !isValidScheme -> return Result.Error(ImageError.INVALID_FORMAT)
            sanitizedUri.length > 256 -> return Result.Error(ImageError.PATH_TOO_LONG)
        }

        val fileSizeMB = imageManager.getFileSizeInMb(sanitizedUri)
        if (fileSizeMB > 5.0) {
            return Result.Error(ImageError.FILE_TOO_LARGE)
        }

        return Result.Success(sanitizedUri)
    }
}