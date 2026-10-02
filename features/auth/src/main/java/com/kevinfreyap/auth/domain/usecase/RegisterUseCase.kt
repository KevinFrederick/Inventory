package com.kevinfreyap.auth.domain.usecase

import com.kevinfreyap.auth.domain.error.RegisterError
import com.kevinfreyap.auth.domain.repository.IAuthRepository
import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.util.errorOrNull
import com.kevinfreyap.domain.util.getOrNull
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: IAuthRepository,
    private val validateName: ValidateNameUseCase,
    private val validateEmail: ValidateEmailUseCase,
    private val validatePass: ValidatePasswordUseCase,
    private val validateConfirmPass: ValidateConfirmPassUseCase
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        pass: String,
        confirmPass: String
    ): Result<Unit, RegisterError> {
        val nameResult = validateName(name)
        val emailResult = validateEmail(email)
        val passResult = validatePass(pass)
        val confirmPassResult = validateConfirmPass(pass, confirmPass)

        if (
            nameResult is Result.Error ||
            emailResult is Result.Error ||
            passResult is Result.Error ||
            confirmPassResult is Result.Error
        ) {
            return Result.Error(
                RegisterError(
                    nameError = nameResult.errorOrNull(),
                    emailError = emailResult.errorOrNull(),
                    passError = passResult.errorOrNull(),
                    confirmPassError = confirmPassResult.errorOrNull()
                )
            )
        }

        val networkResult = authRepository.register(
            name = requireNotNull(nameResult.getOrNull()),
            email = requireNotNull(emailResult.getOrNull()),
            pass = requireNotNull(passResult.getOrNull()),
            confirmPass = requireNotNull(confirmPassResult.getOrNull())
        )

        return when(networkResult) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(
                RegisterError(
                    networkError =  networkResult.error
                )
            )
        }
    }
}