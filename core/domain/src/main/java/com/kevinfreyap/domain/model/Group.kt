package com.kevinfreyap.domain.model

@JvmInline
value class GroupId(val value: String)

data class Group(
    val groupId: GroupId,
    val name: String,
    val description: String?,
    val address: String?,
    val currentUserRole: AppRole,
    val createdAt: Long,
    val lastUpdated: Long
)
