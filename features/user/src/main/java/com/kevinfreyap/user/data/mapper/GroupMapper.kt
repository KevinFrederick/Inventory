package com.kevinfreyap.user.data.mapper

import com.kevinfreyap.database.entity.GroupEntity
import com.kevinfreyap.database.entity.GroupMemberEntity
import com.kevinfreyap.domain.model.AppRole
import com.kevinfreyap.domain.model.Group
import com.kevinfreyap.domain.model.GroupId
import com.kevinfreyap.domain.model.UserId
import com.kevinfreyap.user.data.network.dto.request.AddMemberRequest
import com.kevinfreyap.user.data.network.dto.request.GroupRequest
import com.kevinfreyap.user.data.network.dto.request.UpdateMemberRoleRequest
import com.kevinfreyap.user.data.network.dto.response.GroupMemberResponse
import com.kevinfreyap.network.dto.response.UserGroupResponse
import com.kevinfreyap.user.domain.model.GroupAddUserParams
import com.kevinfreyap.user.domain.model.GroupMember
import com.kevinfreyap.user.domain.model.GroupParams
import com.kevinfreyap.user.domain.model.GroupUpdateRoleParams

fun GroupEntity.toDomain(): Group =
    Group(
        groupId = GroupId(groupId),
        name = name,
        description = description,
        address = address,
        currentUserRole = AppRole.valueOf(currentUserRole),
        createdAt = createdAt,
        lastUpdated = lastUpdated
    )

fun GroupMemberEntity.toDomain(): GroupMember =
    GroupMember(
        userId = UserId(userId),
        avatarUrl = avatarUrl,
        name = name,
        email = email,
        role = AppRole.valueOf(role),
        joinedAt = joinedAt
    )

fun GroupParams.toRequest(): GroupRequest =
    GroupRequest(
        name = name,
        description = description,
        address = address
    )

fun GroupAddUserParams.toRequest(): AddMemberRequest =
    AddMemberRequest(
        email = targetEmail,
        role = assignRole.name
    )

fun GroupUpdateRoleParams.toRequest(): UpdateMemberRoleRequest =
    UpdateMemberRoleRequest(
        role = newRole.name
    )

fun UserGroupResponse.toEntity(): GroupEntity =
    GroupEntity(
        groupId = this.group.groupId,
        name = this.group.name,
        description = this.group.description,
        address = this.group.address,
        currentUserRole = this.role,
        createdAt = this.group.createdAt,
        lastUpdated = this.group.lastUpdated
    )

fun GroupMemberResponse.toEntity(groupId: GroupId): GroupMemberEntity =
    GroupMemberEntity(
        groupId = groupId.value,
        userId = this.userId,
        avatarUrl = this.avatarUrl,
        name = this.name,
        email = this.email,
        role = this.role,
        joinedAt = this.joinedAt
    )