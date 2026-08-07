package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.Result
import com.kevinfreyap.product.domain.model.error.ProductMinimumQuantityError
import javax.inject.Inject

class ValidateProductMinimumQuantityUseCase @Inject constructor() {
    operator fun invoke(
        rawQuantity: String,
        isConfirmed: Boolean
    ): Result<Int, ProductMinimumQuantityError> {
        if (rawQuantity.isBlank()) return Result.Success(0)

        val sanitizedQuantity = rawQuantity.trim()

        val parsedQuantity = sanitizedQuantity.toIntOrNull()
            ?: return Result.Error(ProductMinimumQuantityError.INVALID_FORMAT)

        return when {
            parsedQuantity < 0 -> Result.Error(ProductMinimumQuantityError.CANNOT_BE_NEGATIVE)
            parsedQuantity > 100_000 && !isConfirmed -> Result.Error(ProductMinimumQuantityError.REQUIRES_CONFIRMATION)
            else -> Result.Success(parsedQuantity)
        }
    }
}