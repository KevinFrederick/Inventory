package com.kevinfreyap.user.presentation.mapper

import com.kevinfreyap.domain.model.Group
import com.kevinfreyap.user.presentation.state.GroupListItemState

fun Group.toGroupItem(activeId: String?): GroupListItemState =
    GroupListItemState(
        isActive = groupId.value == activeId,
        groupId = groupId.value,
        name = name,
        totalProduct = totalProducts,
        totalMember = totalMembers,
        status = currentUserStatus
    )