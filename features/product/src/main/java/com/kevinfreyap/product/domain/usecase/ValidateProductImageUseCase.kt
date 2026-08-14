package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.Result
import com.kevinfreyap.product.domain.model.error.ProductImageError
import com.kevinfreyap.product.domain.repository.IImageManager
import javax.inject.Inject

class ValidateProductImageUseCase @Inject constructor(
    private val imageManager: IImageManager
) {
    suspend operator fun invoke(imageUri: String?): Result<String?, ProductImageError> {
        if (imageUri.isNullOrBlank()) return Result.Success(null)

        val sanitizedUri = imageUri.trim()

        val isValidScheme = sanitizedUri.startsWith("content://") ||
                            sanitizedUri.startsWith("file://") ||
                            sanitizedUri.startsWith("http://") ||
                            sanitizedUri.startsWith("https://") ||
                            sanitizedUri.startsWith("/")

        when {
            !isValidScheme -> return Result.Error(ProductImageError.INVALID_FORMAT)
            sanitizedUri.length > 1000 -> return Result.Error(ProductImageError.PATH_TOO_LONG)
        }

        val fileSizeMB = imageManager.getFileSizeInMb(sanitizedUri)
        if (fileSizeMB > 2.0) {
            return Result.Error(ProductImageError.FILE_TOO_LARGE)
        }

        return Result.Success(sanitizedUri)
    }
}