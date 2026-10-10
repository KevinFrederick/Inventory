package com.kevinfreyap.user.domain.validation

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.util.cleanInlineSpaces
import com.kevinfreyap.user.domain.validation.error.GroupNameError
import javax.inject.Inject

class ValidateGroupName @Inject constructor() {
    operator fun invoke(rawName: String): Result<String, GroupNameError> {
        val sanitizedName = rawName.cleanInlineSpaces()

        return when {
            sanitizedName.isBlank() -> Result.Error(GroupNameError.EMPTY)
            sanitizedName.length > 255 -> Result.Error(GroupNameError.TOO_LONG)
            sanitizedName.contains('\n') -> Result.Error(GroupNameError.CONTAINS_NEWLINE)
            else -> Result.Success(sanitizedName)
        }
    }
}