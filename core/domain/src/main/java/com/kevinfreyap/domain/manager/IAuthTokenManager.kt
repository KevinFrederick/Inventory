package com.kevinfreyap.domain.manager

import kotlinx.coroutines.flow.StateFlow

interface IAuthTokenManager {
    val tokenState: StateFlow<String?>
    fun saveToken(accessToken: String, refreshToken: String)
    fun clearToken()
    fun fetchAccessToken(): String?
    fun fetchRefreshToken(): String?
}