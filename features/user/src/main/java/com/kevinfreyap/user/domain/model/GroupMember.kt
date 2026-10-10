package com.kevinfreyap.user.domain.model

import com.kevinfreyap.domain.model.AppRole
import com.kevinfreyap.domain.model.UserId

data class GroupMember(
    val userId: UserId,
    val avatarUrl: String?,
    val name: String,
    val email: String,
    val role: AppRole,
    val joinedAt: Long
)
