package com.kevinfreyap.user.domain.validation

import com.kevinfreyap.domain.Result
import com.kevinfreyap.user.domain.validation.error.UserPhoneNumberError
import javax.inject.Inject

class ValidateUserPhoneNumber @Inject constructor() {
    private val digitsOnlyRegex = "^\\d+\$".toRegex()

    operator fun invoke(rawPhoneNumber: String?): Result<String?, UserPhoneNumberError> {
        if (rawPhoneNumber.isNullOrBlank()) return Result.Success(null)

        val sanitizedPhoneNumber = rawPhoneNumber.trim()

        return when {
            sanitizedPhoneNumber.length > 32 -> Result.Error(UserPhoneNumberError.TOO_LONG)
            !sanitizedPhoneNumber.matches(digitsOnlyRegex) -> Result.Error(UserPhoneNumberError.INVALID_FORMAT)
            else -> {
                val localNumber = if (sanitizedPhoneNumber.startsWith("0")) {
                    sanitizedPhoneNumber.drop(1)
                } else {
                    sanitizedPhoneNumber
                }

                val formattedNumber = "+62$localNumber"

                Result.Success(formattedNumber)
            }
        }
    }
}