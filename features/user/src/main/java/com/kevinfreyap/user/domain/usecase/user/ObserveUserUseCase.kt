package com.kevinfreyap.user.domain.usecase.user

import com.kevinfreyap.domain.model.User
import com.kevinfreyap.user.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserUseCase @Inject constructor(
    private val userRepository: IUserRepository
) {
    operator fun invoke(): Flow<User?> {
        return userRepository.getUser()
    }
}