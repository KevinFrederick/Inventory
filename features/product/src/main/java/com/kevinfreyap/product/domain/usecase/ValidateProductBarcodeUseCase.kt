package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.Result
import com.kevinfreyap.product.domain.model.error.ProductBarcodeError
import com.kevinfreyap.product.domain.repository.IProductRepository
import javax.inject.Inject

class ValidateProductBarcodeUseCase @Inject constructor(
    private val repository: IProductRepository
) {
    suspend operator fun invoke(rawBarcode: String?): Result<String?, ProductBarcodeError> {
        if (rawBarcode.isNullOrBlank()) return Result.Success(null)

        val sanitizedBarcode = rawBarcode.trim()

        when {
            sanitizedBarcode.length > 64 -> return Result.Error(ProductBarcodeError.TOO_LONG)
            sanitizedBarcode.contains(Regex("\\s")) -> return Result.Error(ProductBarcodeError.CONTAINS_WHITESPACE)
            !sanitizedBarcode.matches(Regex("^[\\x21-\\x7E]+$")) -> return Result.Error(ProductBarcodeError.INVALID_CHARACTER)
        }

        val isDuplicate = repository.isBarcodeDuplicate(sanitizedBarcode)
        if (isDuplicate) return Result.Error(ProductBarcodeError.ALREADY_EXISTS)

        return Result.Success(sanitizedBarcode)
    }
}