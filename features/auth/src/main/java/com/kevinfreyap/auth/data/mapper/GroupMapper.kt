package com.kevinfreyap.auth.data.mapper

import com.kevinfreyap.database.entity.GroupEntity
import com.kevinfreyap.network.dto.response.UserGroupResponse

internal fun UserGroupResponse.toEntity(): GroupEntity =
    GroupEntity(
        groupId = this.group.groupId,
        name = this.group.name,
        description = this.group.description,
        address = this.group.address,
        currentUserRole = this.role,
        createdAt = this.group.createdAt,
        lastUpdated = this.group.lastUpdated
    )