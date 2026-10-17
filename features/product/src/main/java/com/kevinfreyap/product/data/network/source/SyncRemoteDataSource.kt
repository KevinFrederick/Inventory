package com.kevinfreyap.product.data.network.source

import android.util.Log
import com.kevinfreyap.domain.Result
import com.kevinfreyap.product.data.network.dto.sync.SyncPayloadDto
import com.kevinfreyap.product.data.network.dto.sync.SyncPullResponseDto
import com.kevinfreyap.product.data.network.dto.sync.SyncPushResponseDto
import com.kevinfreyap.product.data.network.resources.ProductResource
import com.kevinfreyap.product.data.network.resources.SyncResource
import com.kevinfreyap.domain.error.NetworkError
import com.kevinfreyap.domain.manager.IAuthTokenManager
import com.kevinfreyap.network.dto.request.RefreshTokenRequest
import com.kevinfreyap.network.dto.response.TokenResponse
import com.kevinfreyap.network.resource.GroupResource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.plugins.resources.put
import io.ktor.client.plugins.websocket.wss
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import kotlinx.io.IOException
import java.io.File
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class SyncRemoteDataSource @Inject constructor(
    private val client: HttpClient,
    private val tokenManager: IAuthTokenManager
) {
    suspend fun pushSync(groupId: String, payload: SyncPayloadDto): Result<SyncPushResponseDto, NetworkError> {
        return try {
            val response = client.post(SyncResource(
                parent = GroupResource.Id(
                    parent = GroupResource(),
                    groupId = groupId
                )
            )) {
                contentType(ContentType.Application.Json)
                setBody(payload)
            }

            if (response.status.isSuccess()) {
                val data = response.body<SyncPushResponseDto>()
                Result.Success(data)
            } else {
                val errorBody = response.bodyAsText()
                Log.e("PushSync", "Server rejected with ${response.status.value}: $errorBody")

                Result.Error(NetworkError.Server(errorBody))
            }
        } catch (e: ResponseException) {
            val serverMessage = e.response.bodyAsText()
            Result.Error(NetworkError.Server(serverMessage))

        }catch (_: IOException) {
            Result.Error(NetworkError.Local.NO_INTERNET)

        } catch (_: Exception) {
            Result.Error(NetworkError.Local.UNKNOWN)
        }
    }

    suspend fun pullSync(groupId: String, timestamp: Long): Result<SyncPullResponseDto, NetworkError> {
        return try {
            val response = client.get(
                SyncResource.Pull(
                    parent = SyncResource(
                        parent = GroupResource.Id(
                            parent = GroupResource(),
                            groupId = groupId
                        )
                    ),
                    updatedAfter = timestamp
                )
            ).body<SyncPullResponseDto>()

            Result.Success(response)
        } catch (e: ResponseException) {
            val serverMessage = e.response.bodyAsText()
            Result.Error(NetworkError.Server(serverMessage))

        }catch (_: IOException) {
            Result.Error(NetworkError.Local.NO_INTERNET)

        } catch (_: Exception) {
            Result.Error(NetworkError.Local.UNKNOWN)
        }
    }

    suspend fun uploadImage(productId: String, groupId: String, file: File): Result<String, NetworkError> {
        return try {
            val resource = ProductResource.Id.ProductImage(
                parent = ProductResource.Id(
                    parent = ProductResource(
                        parent = GroupResource.Id(
                            parent = GroupResource(),
                            groupId = groupId
                        )
                    ),
                    id = productId
                )
            )

            val mimeType = when (file.extension.lowercase()) {
                "png" -> "image/png"
                "jpg", "jpeg" -> "image/jpeg"
                "webp" -> "image/webp"
                else -> "application/octet-stream" // Safe fallback for unknown binary data
            }

            val response = client.put(resource) {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("image", file.readBytes(), Headers.build {
                                append(HttpHeaders.ContentType, mimeType)
                                append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
                            })
                        }
                    )
                )
            }

            if (response.status.isSuccess()) {
                val data = response.bodyAsText()
                Result.Success(data)
            } else {
                val errorBody = response.bodyAsText()
                Log.e("UploadImage", "Server rejected with ${response.status.value}: $errorBody")

                Result.Error(NetworkError.Server(errorBody))
            }
        } catch (_: Exception) {
            Result.Error(NetworkError.Local.UNKNOWN)
        }
    }

    fun observeSyncPings(groupId: String): Flow<Unit> = flow {
        client.wss(
            urlString = "/sync/ws/${groupId}",
            request = {
                val token = tokenManager.fetchAccessToken()
                if (token != null) {
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        ) {
            for (frame in incoming) {
                if (frame is Frame.Text && frame.readText() == "SYNC_REQUIRED") {
                    emit(Unit)
                }
            }
        }
    }.retryWhen { cause, _ ->
        if (cause is ResponseException && cause.response.status == HttpStatusCode.Unauthorized) {
            Log.w("SyncRemoteDataSource", "WebSocket handshake 401. Attempting token refresh...")
            val refreshed = refreshToken()
            if (!refreshed) {
                Log.e("SyncRemoteDataSource", "Token refresh failed. Stopping WebSocket retries.")
                return@retryWhen false
            }
        } else {
            // Retry connection drops after 5 seconds
            delay(5.seconds)
        }
        true
    }

    private suspend fun refreshToken(): Boolean {
        val refreshToken = tokenManager.fetchRefreshToken() ?: return false
        return try {
            val response = client.post("/auth/refresh") {
                contentType(ContentType.Application.Json)
                setBody(RefreshTokenRequest(refreshToken = refreshToken))
            }
            if (response.status.isSuccess()) {
                val tokenResponse = response.body<TokenResponse>()
                tokenManager.saveToken(
                    accessToken = tokenResponse.accessToken,
                    refreshToken = tokenResponse.refreshToken
                )
                true
            } else {
                if (response.status == HttpStatusCode.Unauthorized || response.status == HttpStatusCode.BadRequest) {
                    tokenManager.clearToken()
                }
                false
            }
        } catch (_: Exception) {
            false
        }
    }
}