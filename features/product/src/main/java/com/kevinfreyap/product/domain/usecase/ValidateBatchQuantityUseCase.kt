package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.Result
import com.kevinfreyap.product.domain.model.error.BatchQuantityError
import javax.inject.Inject

class ValidateBatchQuantityUseCase @Inject constructor() {
    operator fun invoke(
        rawQuantity: String,
        isConfirmed: Boolean
    ): Result<Int, BatchQuantityError> {
        if (rawQuantity.isBlank()) return Result.Error(BatchQuantityError.EMPTY)

        val sanitizedQuantity = rawQuantity.trim()

        val parsedQuantity = sanitizedQuantity.toIntOrNull()
            ?: return Result.Error(BatchQuantityError.INVALID_FORMAT)

        return when {
            parsedQuantity < 0 -> Result.Error(BatchQuantityError.CANNOT_BE_NEGATIVE)
            parsedQuantity == 0 -> Result.Error(BatchQuantityError.CANNOT_BE_ZERO)
            parsedQuantity > 100_000 && !isConfirmed -> Result.Error(BatchQuantityError.REQUIRES_CONFIRMATION)
            else -> Result.Success(parsedQuantity)
        }
    }
}