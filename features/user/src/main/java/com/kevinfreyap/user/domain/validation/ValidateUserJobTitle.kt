package com.kevinfreyap.user.domain.validation

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.util.cleanInlineSpaces
import com.kevinfreyap.user.domain.validation.error.UserOptionalFieldError
import javax.inject.Inject

class ValidateUserJobTitle @Inject constructor() {
    operator fun invoke(rawJobTitle: String?): Result<String?, UserOptionalFieldError> {
        if (rawJobTitle.isNullOrBlank()) return Result.Success(null)

        val sanitized = rawJobTitle.cleanInlineSpaces()

        return when {
            sanitized.length > 128 -> Result.Error(UserOptionalFieldError.TOO_LONG)
            sanitized.contains('\n') -> Result.Error(UserOptionalFieldError.CONTAINS_NEWLINE)
            else -> Result.Success(sanitized)
        }
    }
}