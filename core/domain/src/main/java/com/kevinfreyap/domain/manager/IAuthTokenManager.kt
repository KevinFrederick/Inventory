package com.kevinfreyap.domain.manager

import kotlinx.coroutines.flow.StateFlow

interface IAuthTokenManager {
    val tokenState: StateFlow<String?>
    suspend fun saveToken(accessToken: String, refreshToken: String)
    suspend fun clearToken()
    fun fetchAccessToken(): String?
    fun fetchRefreshToken(): String?
    suspend fun awaitRefreshToken(): String?
}