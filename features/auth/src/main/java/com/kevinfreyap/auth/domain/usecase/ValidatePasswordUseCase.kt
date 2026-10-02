package com.kevinfreyap.auth.domain.usecase

import com.kevinfreyap.auth.domain.error.AuthPasswordError
import com.kevinfreyap.domain.Result
import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {
    operator fun invoke(rawPassword: String): Result<String, AuthPasswordError> {
        return when {
            rawPassword.isBlank() -> Result.Error(AuthPasswordError.EMPTY)
            rawPassword.length < 8 -> Result.Error(AuthPasswordError.TOO_SHORT)
            rawPassword.length > 72 -> Result.Error(AuthPasswordError.TOO_LONG)
            !rawPassword.any { it.isUpperCase() } -> Result.Error(AuthPasswordError.NO_UPPERCASE)
            !rawPassword.any { it.isDigit() } -> Result.Error(AuthPasswordError.NO_DIGIT)
            else -> Result.Success(rawPassword)
        }
    }
}