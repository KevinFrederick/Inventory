package com.kevinfreyap.auth.data.mapper

import com.kevinfreyap.database.entity.GroupEntity
import com.kevinfreyap.network.dto.response.UserGroupResponse

internal fun UserGroupResponse.toEntity(): GroupEntity =
    GroupEntity(
        groupId = this.group.groupId,
        name = this.group.name,
        description = this.group.description,
        address = this.group.address,
        totalMembers = this.group.totalMembers,
        totalProducts = this.group.totalProducts,
        currentUserRole = this.role,
        currentUserStatus = this.status,
        createdAt = this.group.createdAt,
        lastUpdated = this.group.lastUpdated
    )