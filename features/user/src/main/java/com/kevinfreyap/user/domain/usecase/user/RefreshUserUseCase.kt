package com.kevinfreyap.user.domain.usecase.user

import com.kevinfreyap.user.domain.repository.IUserRepository
import javax.inject.Inject

class RefreshUserUseCase @Inject constructor(
    private val userRepository: IUserRepository
) {
    suspend operator fun invoke() {
        userRepository.refreshProfile()
    }
}