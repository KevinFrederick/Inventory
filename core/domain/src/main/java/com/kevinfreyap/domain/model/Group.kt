package com.kevinfreyap.domain.model

import model.MembershipStatus

@JvmInline
value class GroupId(val value: String)

data class Group(
    val groupId: GroupId,
    val name: String,
    val description: String?,
    val address: String?,
    val totalMembers: Int,
    val totalProducts: Int,
    val currentUserRole: AppRole,
    val currentUserStatus: MembershipStatus,
    val createdAt: Long,
    val lastUpdated: Long
)
