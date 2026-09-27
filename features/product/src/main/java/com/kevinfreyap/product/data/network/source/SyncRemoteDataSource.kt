package com.kevinfreyap.product.data.network.source

import android.util.Log
import com.kevinfreyap.domain.Result
import com.kevinfreyap.product.data.network.dto.sync.SyncPayloadDto
import com.kevinfreyap.product.data.network.dto.sync.SyncPullResponseDto
import com.kevinfreyap.product.data.network.dto.sync.SyncPushResponseDto
import com.kevinfreyap.product.data.network.resources.ProductResource
import com.kevinfreyap.product.data.network.resources.SyncResource
import com.kevinfreyap.product.domain.model.error.NetworkError
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.plugins.resources.put
import io.ktor.client.plugins.websocket.wss
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
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
    private val client: HttpClient
) {
    suspend fun pushSync(payload: SyncPayloadDto): Result<SyncPushResponseDto, NetworkError> {
        return try {
            val response = client.post(SyncResource()) {
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

    suspend fun pullSync(timestamp: Long): Result<SyncPullResponseDto, NetworkError> {
        return try {
            val response = client.get(
                SyncResource.Pull(
                    parent = SyncResource(),
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

    suspend fun uploadImage(productId: String, file: File): Result<String, NetworkError> {
        return try {
            val resource = ProductResource.Id.ProductImage(
                parent = ProductResource.Id(id = productId)
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

    fun observeSyncPings(): Flow<Unit> = flow {
        client.wss("/sync/ws") {
            for (frame in incoming) {
                if (frame is Frame.Text && frame.readText() == "SYNC_REQUIRED") {
                    emit(Unit)
                }
            }
        }
    }.retryWhen { _, _ ->
        // If connection drops wait 5 seconds
        delay(5.seconds)
        true
    }
}