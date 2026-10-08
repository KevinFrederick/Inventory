package com.kevinfreyap.user.domain.validation

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.util.cleanInlineSpaces
import com.kevinfreyap.user.domain.validation.error.UserOptionalFieldError
import javax.inject.Inject
import kotlin.text.contains

class ValidateUserUrl @Inject constructor() {
    operator fun invoke (rawUrl: String?): Result<String?, UserOptionalFieldError> {
        val sanitizedUrl = rawUrl?.cleanInlineSpaces() ?: return Result.Success(null)

        return when{
            sanitizedUrl.length > 256 -> Result.Error(UserOptionalFieldError.TOO_LONG)
            sanitizedUrl.contains('\n') -> Result.Error(UserOptionalFieldError.CONTAINS_NEWLINE)
            else ->  Result.Success(sanitizedUrl)
        }
    }
}