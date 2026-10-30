package com.kevinfreyap.domain.usecases

import com.kevinfreyap.domain.model.User
import com.kevinfreyap.domain.repository.ICoreUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUserUseCase @Inject constructor(
    private val userRepository: ICoreUserRepository
) {
    operator fun invoke(): Flow<User?> {
        return userRepository.getUser()
    }
}