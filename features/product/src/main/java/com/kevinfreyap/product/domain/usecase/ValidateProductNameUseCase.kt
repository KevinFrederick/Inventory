package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.util.cleanInlineSpaces
import com.kevinfreyap.product.domain.model.error.ProductNameError
import javax.inject.Inject

class ValidateProductNameUseCase @Inject constructor() {
    operator fun invoke(rawName: String): Result<String, ProductNameError> {
        val sanitizedName = rawName.cleanInlineSpaces()

        return when {
            sanitizedName.isBlank() -> Result.Error(ProductNameError.EMPTY)
            sanitizedName.length > 255 -> Result.Error(ProductNameError.TOO_LONG)
            sanitizedName.contains('\n') -> Result.Error(ProductNameError.CONTAINS_NEWLINE)
            else -> Result.Success(sanitizedName)
        }
    }
}