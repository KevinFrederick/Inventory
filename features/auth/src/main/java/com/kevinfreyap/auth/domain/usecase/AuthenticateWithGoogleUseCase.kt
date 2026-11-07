package com.kevinfreyap.auth.domain.usecase

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.error.NetworkError
import com.kevinfreyap.domain.repository.IAuthRepository
import javax.inject.Inject

class AuthenticateWithGoogleUseCase @Inject constructor(
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(token: String): Result<Unit, NetworkError> {
        if (token.isBlank()) {
            return Result.Error(NetworkError.Local.UNKNOWN)
        }

        return authRepository.authWithGoogle(token)
    }
}