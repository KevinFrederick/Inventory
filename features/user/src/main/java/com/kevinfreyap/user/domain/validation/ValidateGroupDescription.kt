package com.kevinfreyap.user.domain.validation

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.util.cleanInlineSpaces
import com.kevinfreyap.user.domain.validation.error.GroupOptionalFieldError
import javax.inject.Inject

class ValidateGroupDescription @Inject constructor() {
    operator fun invoke(rawDescription: String?): Result<String?, GroupOptionalFieldError> {
        if (rawDescription.isNullOrBlank()) {
            return Result.Success(null)
        }

        val sanitizedDescription = rawDescription.cleanInlineSpaces()

        return when {
            sanitizedDescription.length > 2000 -> Result.Error(GroupOptionalFieldError.TOO_LONG)
            else -> Result.Success(sanitizedDescription)
        }
    }
}