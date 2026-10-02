package com.kevinfreyap.auth.domain.usecase

import com.kevinfreyap.auth.domain.error.AuthEmailError
import com.kevinfreyap.domain.Result
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor() {
    private val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()

    operator fun invoke(rawEmail: String): Result<String, AuthEmailError> {
        val sanitizedEmail = rawEmail.trim()

        return when {
            sanitizedEmail.isBlank() -> Result.Error(AuthEmailError.EMPTY)
            sanitizedEmail.length > 255 -> Result.Error(AuthEmailError.TOO_LONG)
            !sanitizedEmail.matches(emailPattern) -> Result.Error(AuthEmailError.INVALID_FORMAT)
            else -> Result.Success(sanitizedEmail)
        }
    }
}