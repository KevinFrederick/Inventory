package com.kevinfreyap.user.data.repository

import com.kevinfreyap.database.dao.GroupDao
import com.kevinfreyap.database.dao.UserDao
import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.model.Group
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.model.UserId
import com.kevinfreyap.network.error.NetworkError
import com.kevinfreyap.user.data.mapper.toDomain
import com.kevinfreyap.user.data.mapper.toEntity
import com.kevinfreyap.user.data.mapper.toRequest
import com.kevinfreyap.user.data.network.service.GroupApiService
import com.kevinfreyap.user.domain.model.GroupAddUserParams
import com.kevinfreyap.user.domain.model.GroupMember
import com.kevinfreyap.user.domain.model.GroupParams
import com.kevinfreyap.user.domain.model.GroupUpdateRoleParams
import com.kevinfreyap.user.domain.repository.IGroupRepository
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.io.IOException
import javax.inject.Inject

class GroupRepository @Inject constructor(
    private val groupDao: GroupDao,
    private val userDao: UserDao,
    private val api: GroupApiService
): IGroupRepository {
    override fun getGroupById(groupId: GroupId): Flow<Group?> {
        return groupDao.observeGroup(groupId.value).map { it?.toDomain() }
    }

    override fun getTotalGroupCount(): Flow<Int> {
        return groupDao.observeTotalGroupsCount()
    }

    override fun getGroupsForUser(): Flow<List<Group>> {
        return groupDao.observeGroups().map { entities -> entities.map { it.toDomain() } }
    }

    override fun getPreviewGroups(): Flow<List<Group>> {
        return groupDao.observePreviewGroups().map { entities -> entities.map { it.toDomain() } }
    }

    override fun getMemberCount(groupIds: List<GroupId>): Flow<Map<String, Int>> {
        return groupDao.observeMemberCount(groupIds.map { it.value })
    }

    override fun getMembersInGroup(groupId: GroupId): Flow<List<GroupMember>> {
        return groupDao.getGroupMembers(groupId.value).map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun createGroup(newGroup: GroupParams): Result<Unit, NetworkError> {
        return try {
            val response = api.createGroup(newGroup.toRequest())

            groupDao.insertOrUpdateGroups(listOf(response.toEntity()))

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

    override suspend fun updateGroup(
        groupId: GroupId,
        newGroup: GroupParams
    ): Result<Unit, NetworkError> {
        return try {
            val response = api.updateGroup(
                groupId = groupId,
                request = newGroup.toRequest()
            )

            groupDao.insertOrUpdateGroups(listOf(response.toEntity()))

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

    override suspend fun deleteGroup(groupId: GroupId): Result<Unit, NetworkError> {
        return try {
            api.deleteGroup(groupId)

            groupDao.deleteGroup(groupId.value)

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

    override suspend fun addMemberToGroup(groupId: GroupId, req: GroupAddUserParams): Result<Unit, NetworkError> {
        return try {
            val response = api.addMemberToGroup(
                groupId = groupId,
                request = req.toRequest()
            )
            
            groupDao.insertOrUpdateGroupMembers(listOf(response.toEntity(groupId)))

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

    override suspend fun updateMemberRole(targetUserId: UserId, groupId: GroupId, req: GroupUpdateRoleParams): Result<Unit, NetworkError> {
        return try {
            val response = api.updateMemberRole(
                targetUserId = targetUserId,
                groupId = groupId,
                request = req.toRequest()
            )

            groupDao.insertOrUpdateGroupMembers(listOf(response.toEntity(groupId)))

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

    override suspend fun removeMember(
        groupId: GroupId,
        targetUserId: UserId
    ): Result<Unit, NetworkError> {
        return try {
            api.removeMember(groupId, targetUserId)

            val loggedInUserId = userDao.getLoggedInUserId()

            if (targetUserId.value == loggedInUserId) {
                groupDao.deleteGroup(groupId.value)
            } else {
                groupDao.removeMember(groupId.value, targetUserId.value)
            }

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

    override suspend fun refreshGroups() {
        try {
            val response = api.getGroups()
            groupDao.replaceAllGroups(response.map { it.toEntity() })
        } catch (_: Exception) {}
    }

    override suspend fun refreshGroupMembers(groupId: GroupId) {
        try {
            val response = api.getGroupMembers(groupId)
            groupDao.replaceGroupMembers(groupId.value, response.map { it.toEntity(groupId) })
        } catch (_: Exception) {}
    }
}