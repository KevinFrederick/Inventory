package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.Result
import com.kevinfreyap.product.domain.model.error.ProductSkuError
import com.kevinfreyap.product.domain.repository.IProductRepository
import javax.inject.Inject

class ValidateProductSkuUseCase @Inject constructor(
    private val repository: IProductRepository
) {
    suspend operator fun invoke(rawSKU: String?): Result<String?, ProductSkuError> {
        if (rawSKU.isNullOrBlank()) return Result.Success(null)

        val sanitizedSku = rawSKU
            .trim()
            .uppercase()

        when {
            sanitizedSku.length > 128 -> return Result.Error(ProductSkuError.TOO_LONG)
            sanitizedSku.contains(Regex("\\s")) -> return Result.Error(ProductSkuError.CONTAINS_WHITESPACE)
            sanitizedSku.matches(Regex("^[A-Z0-9_\\-]+$")) -> return Result.Error(ProductSkuError.INVALID_CHARACTERS)
        }

        val isDuplicate = repository.isSkuDuplicate(sanitizedSku)
        if (isDuplicate) return Result.Error(ProductSkuError.ALREADY_EXISTS)

        return Result.Success(sanitizedSku)
    }
}