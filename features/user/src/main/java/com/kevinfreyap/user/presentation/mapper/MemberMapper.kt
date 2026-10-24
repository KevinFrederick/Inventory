package com.kevinfreyap.user.presentation.mapper

import com.kevinfreyap.ui.util.toFullDateString
import com.kevinfreyap.user.domain.model.GroupMember
import com.kevinfreyap.user.presentation.state.MemberListItemState
import com.kevinfreyap.user.presentation.state.PendingMemberListItemState

fun GroupMember.toMemberItem(): MemberListItemState =
    MemberListItemState(
        memberId = this.userId.value,
        username = this.name,
        email = this.email,
        role = this.role.name,
        profilePicture = this.avatarUrl
    )

fun GroupMember.toPendingMember(): PendingMemberListItemState =
    PendingMemberListItemState(
        memberId = this.userId.value,
        requestName = this.name,
        requestEmail = this.email,
        requestDate = this.joinedAt.toFullDateString()
    )