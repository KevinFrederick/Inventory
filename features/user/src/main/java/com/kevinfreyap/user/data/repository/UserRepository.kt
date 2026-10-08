package com.kevinfreyap.user.data.repository

import com.kevinfreyap.database.dao.GroupDao
import com.kevinfreyap.database.dao.UserDao
import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.model.User
import com.kevinfreyap.network.error.NetworkError
import com.kevinfreyap.user.data.mapper.toDomain
import com.kevinfreyap.user.data.mapper.toEntity
import com.kevinfreyap.user.data.network.dto.request.UpdateProfileRequest
import com.kevinfreyap.user.data.network.service.UserApiService
import com.kevinfreyap.user.domain.model.UserUpdateParams
import com.kevinfreyap.user.domain.repository.IUserRepository
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.io.IOException
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val groupDao: GroupDao,
    private val api: UserApiService
): IUserRepository {
    override fun getUser(): Flow<User?> {
        return userDao.observeUser().map { it?.toDomain() }
    }

    override suspend fun updateUserProfile(
        req: UserUpdateParams
    ): Result<Unit, NetworkError> {
        return try {
            val request = UpdateProfileRequest(
                name = req.name,
                avatarUrl = req.avatarUrl,
                phoneNumber = req.phoneNumber,
                jobTitle = req.jobTitle,
                locale = req.locale,
                timeZone = req.timeZone
            )

            val updatedUser = api.updateUserProfile(request)

            userDao.insertOrUpdate(updatedUser.toEntity())

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

    override suspend fun refreshProfile() {
        try {
            val response = api.getUser()
            userDao.insertOrUpdate(response.toEntity())
        } catch (_: Exception) { }
    }

    override suspend fun deleteAccount(): Result<Unit, NetworkError> {
        return try {
            api.deleteUser()
            userDao.clearUser()
            groupDao.clearGroups()
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
}