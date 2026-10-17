package com.kevinfreyap.user.presentation.state

import com.kevinfreyap.domain.model.AppRole

data class GroupDetailScreenState(
    val isActive: Boolean,
    val currentUserRole: AppRole,
    val groupDetail: GroupDetailState,
    val members: List<MemberListItemState>
)
