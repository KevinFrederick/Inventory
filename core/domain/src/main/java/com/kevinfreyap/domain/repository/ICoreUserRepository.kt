package com.kevinfreyap.domain.repository

import com.kevinfreyap.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ICoreUserRepository {
    fun getUser(): Flow<User?>
}