package com.kevinfreyap.product.domain.usecase

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.util.cleanInlineSpaces
import com.kevinfreyap.domain.util.toTitleCase
import com.kevinfreyap.product.domain.model.error.BatchLocationError
import javax.inject.Inject

class ValidateBatchLocationUseCase @Inject constructor() {
    operator fun invoke(rawLocation: String): Result<String, BatchLocationError> {
        val sanitizedLocation = rawLocation
            .cleanInlineSpaces()
            .toTitleCase()

        return when {
            sanitizedLocation.isBlank() -> Result.Error(BatchLocationError.EMPTY)
            sanitizedLocation.length > 50 -> Result.Error(BatchLocationError.TOO_LONG)
            sanitizedLocation.contains('\n') -> Result.Error(BatchLocationError.CONTAINS_NEWLINE)
            else -> Result.Success(sanitizedLocation)
        }
    }
}