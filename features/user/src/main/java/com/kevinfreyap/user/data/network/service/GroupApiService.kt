package com.kevinfreyap.user.data.network.service

import api.dto.request.ApproveMemberRequest
import api.dto.request.GenerateInviteRequest
import api.dto.request.JoinGroupRequest
import api.dto.response.InviteResponse
import api.dto.response.PendingMemberResponse
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.model.UserId
import com.kevinfreyap.network.resource.GroupResource
import com.kevinfreyap.user.data.network.dto.request.AddMemberRequest
import com.kevinfreyap.user.data.network.dto.request.GroupRequest
import com.kevinfreyap.user.data.network.dto.request.UpdateMemberRoleRequest
import com.kevinfreyap.user.data.network.dto.response.GroupMemberResponse
import com.kevinfreyap.network.dto.response.UserGroupResponse
import com.kevinfreyap.user.data.network.dto.response.MessageResponse
import com.kevinfreyap.user.data.network.resource.JoinGroupResource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.delete
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.plugins.resources.put
import io.ktor.client.request.setBody
import javax.inject.Inject

class GroupApiService @Inject constructor(
    private val client: HttpClient
) {
    suspend fun createGroup(request: GroupRequest): UserGroupResponse {
        return client.post(GroupResource()) {
            setBody(request)
        }.body()
    }

    suspend fun updateGroup(groupId: GroupId, request: GroupRequest): UserGroupResponse {
        return client.put(GroupResource.Id(
            parent = GroupResource(),
            groupId = groupId.value
        )) {
            setBody(request)
        }.body()
    }

    suspend fun deleteGroup(groupId: GroupId) {
        client.delete(GroupResource.Id(
            parent = GroupResource(),
            groupId = groupId.value
        ))
    }

    suspend fun addMemberToGroup(groupId: GroupId, request: AddMemberRequest): GroupMemberResponse {
        return client.post(GroupResource.Id.Members(
            parent = GroupResource.Id(
                parent = GroupResource(),
                groupId = groupId.value
            )
        )) {
            setBody(request)
        }.body()
    }

    suspend fun updateMemberRole(targetUserId: UserId, groupId: GroupId, request: UpdateMemberRoleRequest): GroupMemberResponse {
        return client.put(GroupResource.Id.Members.Member(
            parent = GroupResource.Id.Members(
                parent = GroupResource.Id(
                    parent = GroupResource(),
                    groupId = groupId.value
                ),
            ),
            memberId = targetUserId.value
        )) {
            setBody(request)
        }.body()
    }

    suspend fun removeMember(groupId: GroupId, targetUserId: UserId) {
        client.delete(GroupResource.Id.Members.Member(
            parent = GroupResource.Id.Members(
                parent = GroupResource.Id(
                    parent = GroupResource(),
                    groupId = groupId.value
                ),
            ),
            memberId = targetUserId.value
        ))
    }

    suspend fun generateInviteCode(groupId: GroupId, request: GenerateInviteRequest): InviteResponse {
        return client.post(GroupResource.Id.Invites (
            parent = GroupResource.Id(
                parent = GroupResource(),
                groupId = groupId.value
            )
        )) {
            setBody(request)
        }.body()
    }

    suspend fun joinGroup(request: JoinGroupRequest): MessageResponse {
        return client.post(JoinGroupResource()) {
            setBody(request)
        }.body()
    }

    suspend fun getPendingMembers(groupId: GroupId): List<PendingMemberResponse> {
        return client.get(GroupResource.Id.Members.Pending (
            parent = GroupResource.Id.Members(
                parent = GroupResource.Id(
                    parent = GroupResource(),
                    groupId = groupId.value
                )
            )
        )).body()
    }

    suspend fun approvePendingMember(groupId: GroupId, memberId: UserId, request: ApproveMemberRequest): MessageResponse {
        return client.put(GroupResource.Id.Members.Approve(
            parent = GroupResource.Id.Members(
                parent = GroupResource.Id(
                    parent = GroupResource(),
                    groupId = groupId.value
                )
            ),
            targetUserId = memberId.value
        )) {
            setBody(request)
        }.body()
    }

    suspend fun rejectPendingMember(groupId: GroupId, memberId: UserId): MessageResponse {
        return client.put(GroupResource.Id.Members.Reject(
            parent = GroupResource.Id.Members(
                parent = GroupResource.Id(
                    parent = GroupResource(),
                    groupId = groupId.value
                )
            ),
            targetUserId = memberId.value
        )).body()
    }

    suspend fun getGroups(): List<UserGroupResponse> {
        return client.get(GroupResource()).body()
    }

    suspend fun getGroupMembers(groupId: GroupId): List<GroupMemberResponse> {
        return client.get(GroupResource.Id.Members(
            parent = GroupResource.Id(
                parent = GroupResource(),
                groupId = groupId.value
            )
        )).body()
    }
}