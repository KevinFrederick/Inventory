package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.Result
import com.kevinfreyap.product.domain.model.error.ProductBarcodeError
import javax.inject.Inject

class ValidateProductBarcodeUseCase @Inject constructor() {
    operator fun invoke(rawBarcode: String?): Result<String?, ProductBarcodeError> {
        if (rawBarcode.isNullOrBlank()) return Result.Success(null)

        val sanitizedBarcode = rawBarcode.trim()

        return when {
            sanitizedBarcode.length > 50 -> Result.Error(ProductBarcodeError.TOO_LONG)
            sanitizedBarcode.contains(Regex("\\s")) -> Result.Error(ProductBarcodeError.CONTAINS_WHITESPACE)
            !sanitizedBarcode.all { it.isLetterOrDigit() } -> Result.Error(ProductBarcodeError.INVALID_CHARACTER)
            else -> Result.Success(sanitizedBarcode)
        }
    }
}