package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.Result
import com.kevinfreyap.product.domain.model.error.BatchPriceError
import javax.inject.Inject

class ValidateBatchPriceUseCase @Inject constructor() {
    operator fun invoke(
        rawPrice: String,
        isConfirmed: Boolean
    ): Result<Double, BatchPriceError> {
        if (rawPrice.isBlank()) return Result.Success(0.0)

        val sanitizedRawPrice = rawPrice.trim()

        val parsedPrice = sanitizedRawPrice.toDoubleOrNull()
            ?: return Result.Error(BatchPriceError.INVALID_FORMAT)

        return when {
            parsedPrice < 0.0 -> Result.Error(BatchPriceError.CANNOT_BE_NEGATIVE)
            parsedPrice > 10_000_000_000.0 && !isConfirmed -> Result.Error(BatchPriceError.REQUIRES_CONFIRMATION)
            else -> Result.Success(parsedPrice)
        }
    }
}