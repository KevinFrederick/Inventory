package com.kevinfreyap.user.data.network.service

import com.kevinfreyap.network.dto.response.UserResponse
import com.kevinfreyap.user.data.network.dto.request.UpdateProfileRequest
import com.kevinfreyap.user.data.network.resource.UserResource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.delete
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.put
import io.ktor.client.request.setBody
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

    suspend fun deleteUser() {
        client.delete(UserResource())
    }
}