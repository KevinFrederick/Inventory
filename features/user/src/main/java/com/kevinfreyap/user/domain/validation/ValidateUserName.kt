package com.kevinfreyap.user.domain.validation

import com.kevinfreyap.domain.util.cleanInlineSpaces
import com.kevinfreyap.user.domain.validation.error.UserNameError
import com.kevinfreyap.domain.Result
import javax.inject.Inject

class ValidateUserName @Inject constructor() {
    operator fun invoke(rawName: String): Result<String, UserNameError> {
        val sanitizedName = rawName.cleanInlineSpaces()

        return when {
            sanitizedName.isBlank() -> Result.Error(UserNameError.EMPTY)
            sanitizedName.length > 255 -> Result.Error(UserNameError.TOO_LONG)
            sanitizedName.contains('\n') -> Result.Error(UserNameError.CONTAINS_NEWLINE)
            else -> Result.Success(sanitizedName)
        }
    }
}