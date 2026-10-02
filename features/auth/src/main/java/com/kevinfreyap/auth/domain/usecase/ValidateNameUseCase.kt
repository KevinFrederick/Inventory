package com.kevinfreyap.auth.domain.usecase

import com.kevinfreyap.auth.domain.error.AuthNameError
import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.util.cleanInlineSpaces
import javax.inject.Inject

class ValidateNameUseCase @Inject constructor() {
    operator fun invoke(rawName: String): Result<String, AuthNameError> {
        val sanitizedName = rawName.cleanInlineSpaces()

        return when {
            sanitizedName.isBlank() -> Result.Error(AuthNameError.EMPTY)
            sanitizedName.length > 255 -> Result.Error(AuthNameError.TOO_LONG)
            sanitizedName.contains('\n') -> Result.Error(AuthNameError.CONTAINS_NEWLINE)
            else -> Result.Success(sanitizedName)
        }
    }
}