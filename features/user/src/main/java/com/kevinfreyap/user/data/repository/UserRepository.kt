package com.kevinfreyap.user.data.repository

import com.kevinfreyap.database.dao.GroupDao
import com.kevinfreyap.database.dao.UserDao
import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.model.User
import com.kevinfreyap.domain.error.NetworkError
import com.kevinfreyap.domain.repository.ICoreUserRepository
import com.kevinfreyap.user.data.mapper.toDomain
import com.kevinfreyap.user.data.mapper.toEntity
import com.kevinfreyap.user.data.network.dto.request.UpdateProfileRequest
import com.kevinfreyap.user.data.network.service.UserApiService
import com.kevinfreyap.user.domain.model.UserUpdateParams
import com.kevinfreyap.user.domain.repository.IUserRepository
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import java.io.FileNotFoundException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val groupDao: GroupDao,
    private val api: UserApiService
): IUserRepository, ICoreUserRepository {
    override fun getUser(): Flow<User?> {
        return userDao.observeUser().map { it?.toDomain() }
    }

    override suspend fun updateUserProfile(
        req: UserUpdateParams
    ): Result<Unit, NetworkError> {
        return try {
            val request = UpdateProfileRequest(
                name = req.name,
                avatarUrl = req.profilePictureUrl,
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

    override suspend fun uploadProfilePicture(
        filePath: String
    ): Result<String, NetworkError> {
        return withContext(Dispatchers.IO) {
            try {
                val uploadResult = api.uploadProfilePicture(filePath)

                userDao.updateProfilePicture(uploadResult)

                Result.Success(uploadResult)
            } catch (e: ResponseException) {
                val serverMessage = e.response.bodyAsText()
                Result.Error(NetworkError.Server(serverMessage))

            }  catch (_: FileNotFoundException) {
                Result.Error(NetworkError.Local.MISSING_FILE)

            } catch (_: IOException) {
                Result.Error(NetworkError.Local.NO_INTERNET)

            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Result.Error(NetworkError.Local.UNKNOWN)
            }
        }
    }

    override suspend fun refreshProfile() {
        try {
            val response = api.getUser()
            userDao.insertOrUpdate(response.toEntity())
        } catch (_: Exception) { }
    }

    override suspend fun deleteProfilePicture(): Result<Unit, NetworkError> {
        return try {
            api.deleteProfilePicture()
            userDao.deleteProfilePicture()

            Result.Success(Unit)
        } catch (e: ResponseException) {
            if (e.response.status == HttpStatusCode.NotFound) {
                userDao.deleteProfilePicture()
                return Result.Success(Unit)
            }

            val serverMessage = e.response.bodyAsText()
            Result.Error(NetworkError.Server(serverMessage))

        } catch (_: IOException) {
            Result.Error(NetworkError.Local.NO_INTERNET)

        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.Error(NetworkError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteAccount(): Result<Unit, NetworkError> {
        return try {
            api.deleteUser()
            api.deleteProfilePicture()
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