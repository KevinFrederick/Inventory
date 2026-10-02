package com.kevinfreyap.network.client

import com.kevinfreyap.domain.manager.IAuthTokenManager
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.takeFrom
import com.kevinfreyap.network.BuildConfig
import com.kevinfreyap.network.dto.RefreshTokenRequest
import com.kevinfreyap.network.dto.TokenResponse
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.resources.Resources
import io.ktor.client.plugins.resources.post
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

fun provideKtorClient(
    tokenManager: IAuthTokenManager
): HttpClient {
    return HttpClient(OkHttp) {
        expectSuccess = true

        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(DefaultRequest) {
            contentType(ContentType.Application.Json)
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val accessToken = tokenManager.fetchAccessToken()
                    val refreshToken = tokenManager.fetchRefreshToken()

                    if (accessToken != null && refreshToken != null) {
                        BearerTokens(accessToken, refreshToken)
                    } else {
                        null
                    }
                }

                refreshTokens {
                    val refreshToken = tokenManager.fetchRefreshToken() ?: return@refreshTokens null

                    try {
                        val response: HttpResponse = client.post("${BuildConfig.BASE_URL}/auth/refresh") {
                            setBody(RefreshTokenRequest(refreshToken = refreshToken))
                            markAsRefreshTokenRequest()
                        }

                        if (response.status.isSuccess()) {
                            val tokenResponse = response.body<TokenResponse>()

                            tokenManager.saveToken(
                                accessToken = tokenResponse.accessToken,
                                refreshToken = tokenResponse.refreshToken
                            )

                            BearerTokens(tokenResponse.accessToken, tokenResponse.refreshToken)
                        } else {
                            tokenManager.clearToken()
                            null
                        }
                    } catch (_: Exception) {
                        tokenManager.clearToken()
                        null
                    }

                }
            }
        }

        install(Resources)

        install(WebSockets) {
            pingInterval = 20.seconds
        }

        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.INFO
        }

        defaultRequest {
            url {
                takeFrom(BuildConfig.BASE_URL)
                protocol = URLProtocol.HTTPS
            }
        }
    }
}