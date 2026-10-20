package com.kevinfreyap.user.presentation.state

import com.kevinfreyap.domain.model.AppRole

data class GroupMembersScreenState(
    val activeMembers: List<MemberListItemState> = emptyList(),
    val pendingMembers: List<PendingMemberListItemState> = emptyList(),
    val assignableRoles: List<AppRole> = emptyList(),
    val generatedInviteCode: String? = null,
    val loggedInUserRole: AppRole = AppRole.MEMBER
)