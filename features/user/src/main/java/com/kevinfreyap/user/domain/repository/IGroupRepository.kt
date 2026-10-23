package com.kevinfreyap.user.domain.repository

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.model.Group
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.model.UserId
import com.kevinfreyap.domain.error.NetworkError
import com.kevinfreyap.domain.model.AppRole
import com.kevinfreyap.user.domain.model.GroupAddUserParams
import com.kevinfreyap.user.domain.model.GroupMember
import com.kevinfreyap.user.domain.model.GroupParams
import com.kevinfreyap.user.domain.model.GroupUpdateRoleParams
import kotlinx.coroutines.flow.Flow

interface IGroupRepository {
    fun getGroupById(groupId: GroupId): Flow<Group?>
    fun getTotalGroupCount(): Flow<Int>
    fun getPreviewGroups(): Flow<List<Group>>
    fun getMemberCount(groupIds: List<GroupId>): Flow<Map<String, Int>>
    fun getMembersInGroup(groupId: GroupId): Flow<List<GroupMember>>
    fun getMembersPreview(groupId: GroupId): Flow<List<GroupMember>>
    fun observePendingMembers(groupId: GroupId): Flow<List<GroupMember>>
    suspend fun getOwnedGroupCount(): Int

    suspend fun createGroup(newGroup: GroupParams): Result<Unit, NetworkError>
    suspend fun updateGroup(groupId: GroupId, newGroup: GroupParams): Result<Unit, NetworkError>
    suspend fun deleteGroup(groupId: GroupId): Result<Unit, NetworkError>

    suspend fun addMemberToGroup(groupId: GroupId, req: GroupAddUserParams): Result<Unit, NetworkError>
    suspend fun updateMemberRole(targetUserId: UserId, groupId: GroupId, req: GroupUpdateRoleParams): Result<Unit, NetworkError>
    suspend fun removeMember(groupId: GroupId, targetUserId: UserId): Result<Unit, NetworkError>

    suspend fun requestJoinGroup(inviteCode: String): Result<Unit, NetworkError>
    suspend fun createInviteCode(groupId: GroupId, validityInDays: Int): Result<String, NetworkError>
    suspend fun approveMember(groupId: GroupId, memberId: UserId, role: AppRole): Result<Unit, NetworkError>
    suspend fun rejectMember(groupId: GroupId, memberId: UserId): Result<Unit, NetworkError>

    suspend fun refreshGroups()
    suspend fun refreshGroupMembers(groupId: GroupId)
    suspend fun refreshPendingMembers(groupId: GroupId)
}