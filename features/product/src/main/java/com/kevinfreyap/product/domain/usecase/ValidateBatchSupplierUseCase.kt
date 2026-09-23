package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.Result
import com.kevinfreyap.product.domain.model.error.BatchSupplierError
import javax.inject.Inject

class ValidateBatchSupplierUseCase @Inject constructor() {
    operator fun invoke(rawSupplier: String?): Result<String?, BatchSupplierError> {
        if (rawSupplier.isNullOrBlank()) return Result.Success(null)

        val sanitizedSupplier = rawSupplier
            .replace(Regex("\\s+"), " ")
            .trim()

        return when {
            sanitizedSupplier.length > 128 -> Result.Error(BatchSupplierError.TOO_LONG)
            else -> Result.Success(sanitizedSupplier)
        }
    }
}