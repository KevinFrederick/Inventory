package com.kevinfreyap.user.domain.usecase.user

import com.kevinfreyap.domain.Result
import com.kevinfreyap.network.error.NetworkError
import com.kevinfreyap.user.domain.repository.IUserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val userRepository: IUserRepository
) {
    suspend operator fun invoke(): Result<Unit, NetworkError> {
        return userRepository.deleteAccount()
    }
}