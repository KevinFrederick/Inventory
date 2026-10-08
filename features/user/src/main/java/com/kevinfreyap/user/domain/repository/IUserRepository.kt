package com.kevinfreyap.user.domain.repository

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.model.User
import com.kevinfreyap.network.error.NetworkError
import com.kevinfreyap.user.domain.model.UserUpdateParams
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun getUser(): Flow<User?>
    suspend fun updateUserProfile(
        req: UserUpdateParams
    ): Result<Unit, NetworkError>
    suspend fun refreshProfile()
    suspend fun deleteAccount(): Result<Unit, NetworkError>
}