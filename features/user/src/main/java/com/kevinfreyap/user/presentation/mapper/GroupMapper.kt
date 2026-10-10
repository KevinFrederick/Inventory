package com.kevinfreyap.user.presentation.mapper

import com.kevinfreyap.user.domain.model.GroupWithStats
import com.kevinfreyap.user.presentation.state.GroupListItemState

fun GroupWithStats.toGroupItem(): GroupListItemState =
    GroupListItemState(
        groupId = group.groupId.value,
        name = group.name,
        totalProduct = totalProducts,
        totalMember = totalMembers
    )