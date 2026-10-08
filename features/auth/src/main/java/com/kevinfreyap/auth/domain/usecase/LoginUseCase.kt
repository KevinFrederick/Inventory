package com.kevinfreyap.auth.domain.usecase

import com.kevinfreyap.auth.domain.error.AuthPasswordError
import com.kevinfreyap.auth.domain.error.LoginError
import com.kevinfreyap.auth.domain.repository.IAuthRepository
import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.util.errorOrNull
import com.kevinfreyap.domain.util.getOrNull
import com.kevinfreyap.domain.validation.ValidateEmailUseCase
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: IAuthRepository,
    private val validateEmail: ValidateEmailUseCase,
    private val validatePass: ValidatePasswordUseCase
) {
    suspend operator fun invoke(
        email: String,
        pass: String
    ): Result<Unit, LoginError> {
        val emailResult = validateEmail(email)

        val isPasswordEmpty = pass.trim().isEmpty()

        if (
            emailResult is Result.Error ||
            isPasswordEmpty
        ) {
            return Result.Error(
                LoginError(
                    emailError = emailResult.errorOrNull(),
                    passError = if (isPasswordEmpty) AuthPasswordError.EMPTY else null,
                )
            )
        }

        val networkResult = authRepository.login(
            email = requireNotNull(emailResult.getOrNull()),
            pass = pass
        )

        return when(networkResult) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(
                LoginError(
                    networkError = networkResult.error
                )
            )
        }
    }
}