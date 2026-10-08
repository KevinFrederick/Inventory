package com.kevinfreyap.domain.validation

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.error.EmailError
import javax.inject.Inject

class ValidateEmailUseCase @Inject constructor() {
    private val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()

    operator fun invoke(rawEmail: String): Result<String, EmailError> {
        val sanitizedEmail = rawEmail.trim()

        return when {
            sanitizedEmail.isBlank() -> Result.Error(EmailError.EMPTY)
            sanitizedEmail.length > 255 -> Result.Error(EmailError.TOO_LONG)
            !sanitizedEmail.matches(emailPattern) -> Result.Error(EmailError.INVALID_FORMAT)
            else -> Result.Success(sanitizedEmail)
        }
    }
}