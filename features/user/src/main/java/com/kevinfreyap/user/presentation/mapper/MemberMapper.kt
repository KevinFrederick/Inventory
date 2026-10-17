package com.kevinfreyap.user.presentation.mapper

import com.kevinfreyap.user.domain.model.GroupMember
import com.kevinfreyap.user.presentation.state.MemberListItemState

fun GroupMember.toMemberItem(): MemberListItemState =
    MemberListItemState(
        username = this.name,
        email = this.email,
        role = this.role.name,
        profilePicture = this.avatarUrl
    )