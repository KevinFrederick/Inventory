package com.kevinfreyap.user.domain.validation

import com.kevinfreyap.domain.Result
import com.kevinfreyap.user.domain.validation.error.UserPhoneNumberError
import javax.inject.Inject

class ValidateUserPhoneNumber @Inject constructor() {
    private val phoneRegex = "^\\+?[1-9]\\d{1,14}\$".toRegex()

    operator fun invoke(rawPhoneNumber: String?): Result<String?, UserPhoneNumberError> {
        if (rawPhoneNumber.isNullOrBlank()) return Result.Success(null)

        val sanitizedPhoneNumber = rawPhoneNumber.trim()

        return when {
            sanitizedPhoneNumber.length > 32 -> Result.Error(UserPhoneNumberError.TOO_LONG)
            !sanitizedPhoneNumber.matches(phoneRegex) -> Result.Error(UserPhoneNumberError.INVALID_FORMAT)
            else -> Result.Success(sanitizedPhoneNumber)
        }
    }
}