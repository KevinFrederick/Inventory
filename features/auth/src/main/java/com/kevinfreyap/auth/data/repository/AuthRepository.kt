package com.kevinfreyap.auth.data.repository

import api.dto.request.LoginRequest
import api.dto.request.LogoutRequest
import api.dto.request.RegisterRequest
import com.kevinfreyap.auth.data.network.service.AuthApiService
import com.kevinfreyap.auth.domain.repository.IAuthRepository
import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.manager.IAuthTokenManager
import com.kevinfreyap.network.error.NetworkError
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.io.IOException
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthApiService,
    private val tokenManager: IAuthTokenManager
): IAuthRepository {
    override suspend fun register(
        name: String,
        email: String,
        pass: String,
        confirmPass: String
    ): Result<Unit, NetworkError> {
        return try {
            val response = api.register(
                RegisterRequest(
                    name = name,
                    email = email,
                    password = pass,
                    confirmPassword = confirmPass
                )
            )

            tokenManager.saveToken(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken
            )

            // Save User

            Result.Success(Unit)
        } catch (e: ResponseException) {
            val serverMessage = e.response.bodyAsText()
            Result.Error(NetworkError.Server(serverMessage))

        } catch (_: IOException) {
            Result.Error(NetworkError.Local.NO_INTERNET)

        } catch (_: Exception) {
            Result.Error(NetworkError.Local.UNKNOWN)
        }
    }

    override suspend fun login(
        email: String,
        pass: String
    ): Result<Unit, NetworkError> {
        return try {
            val response = api.login(
                LoginRequest(
                    email = email,
                    password = pass
                )
            )

            tokenManager.saveToken(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken
            )

            // Save User

            Result.Success(Unit)
        } catch (e: ResponseException) {
            val serverMessage = e.response.bodyAsText()
            Result.Error(NetworkError.Server(serverMessage))

        } catch (_: IOException) {
            Result.Error(NetworkError.Local.NO_INTERNET)

        } catch (_: Exception) {
            Result.Error(NetworkError.Local.UNKNOWN)
        }
    }

    override suspend fun logout(): Result<Unit, NetworkError> {
        return try {
            val refreshToken = tokenManager.fetchRefreshToken() ?: return Result.Success(Unit)

            api.logout(
                LogoutRequest(
                    refreshToken = refreshToken
                )
            )

            // Clear User

            tokenManager.clearToken()

            Result.Success(Unit)
        } catch (e: ResponseException) {
            val serverMessage = e.response.bodyAsText()
            Result.Error(NetworkError.Server(serverMessage))

        } catch (_: IOException) {
            Result.Error(NetworkError.Local.NO_INTERNET)

        } catch (_: Exception) {
            Result.Error(NetworkError.Local.UNKNOWN)
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return tokenManager.fetchRefreshToken() != null
    }
}