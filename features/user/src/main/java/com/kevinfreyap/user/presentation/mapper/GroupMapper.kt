package com.kevinfreyap.user.presentation.mapper

import com.kevinfreyap.user.domain.model.GroupWithStats
import com.kevinfreyap.user.presentation.state.GroupListItemState

fun GroupWithStats.toGroupItem(activeId: String?): GroupListItemState =
    GroupListItemState(
        isActive = group.groupId.value == activeId,
        groupId = group.groupId.value,
        name = group.name,
        totalProduct = totalProducts,
        totalMember = totalMembers
    )