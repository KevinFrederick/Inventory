package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.util.cleanInlineSpaces
import com.kevinfreyap.product.domain.model.error.ProductDescriptionError
import javax.inject.Inject

class ValidateProductDescriptionUseCase @Inject constructor() {
    operator fun invoke(rawDescription: String?): Result<String?, ProductDescriptionError> {
        if (rawDescription.isNullOrBlank()) {
            return Result.Success(null)
        }

        val sanitizedDescription = rawDescription.cleanInlineSpaces()

        return when {
            sanitizedDescription.length > 500 -> Result.Error(ProductDescriptionError.TOO_LONG)
            else -> Result.Success(sanitizedDescription)
        }
    }
}