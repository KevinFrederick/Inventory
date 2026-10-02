package com.kevinfreyap.auth.domain.usecase

import com.kevinfreyap.auth.domain.error.AuthConfirmPasswordError
import com.kevinfreyap.domain.Result
import javax.inject.Inject

class ValidateConfirmPassUseCase @Inject constructor() {
    operator fun invoke(
        originalPass: String,
        confirmPass: String
    ): Result<String, AuthConfirmPasswordError> {
        return when {
            confirmPass.isBlank() -> Result.Error(AuthConfirmPasswordError.EMPTY)
            originalPass != confirmPass -> Result.Error(AuthConfirmPasswordError.DOES_NOT_MATCH)
            else -> Result.Success(confirmPass)
        }
    }
}