package com.kevinfreyap.user.data.network.service

import com.kevinfreyap.network.dto.response.UserResponse
import com.kevinfreyap.user.data.network.dto.request.UpdateProfileRequest
import com.kevinfreyap.user.data.network.resource.UserResource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.delete
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.put
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Inject

class UserApiService @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getUser(): UserResponse {
        return client.get(UserResource()).body()
    }

    suspend fun updateUserProfile(request: UpdateProfileRequest): UserResponse {
        return client.put(UserResource()) {
            setBody(request)
        }.body()
    }

    suspend fun uploadProfilePicture(localFilePath: String): String {
        val file = File(localFilePath)
        if (!file.exists()) throw FileNotFoundException("File not found")

        val mimeType = when (file.extension.lowercase()) {
            "png" -> "image/png"
            "jpg", "jpeg" -> "image/jpeg"
            "webp" -> "image/webp"
            else -> "application/octet-stream"
        }

        val response = client.put(UserResource.Image(
            parent = UserResource()
        )) {
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

        return response.bodyAsText()
    }

    suspend fun deleteUser() {
        client.delete(UserResource())
    }

    suspend fun deleteProfilePicture() {
        client.delete(UserResource.Image(UserResource()))
    }
}