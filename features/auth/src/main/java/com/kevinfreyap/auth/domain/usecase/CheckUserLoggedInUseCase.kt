package com.kevinfreyap.auth.domain.usecase

import com.kevinfreyap.auth.domain.repository.IAuthRepository
import javax.inject.Inject

class CheckUserLoggedInUseCase @Inject constructor(
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return authRepository.isUserLoggedIn()
    }
}