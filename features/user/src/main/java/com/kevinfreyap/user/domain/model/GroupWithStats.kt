package com.kevinfreyap.user.domain.model

import com.kevinfreyap.domain.model.Group

data class GroupWithStats(
    val group: Group,
    val totalMembers: Int,
    val totalProducts: Int
)
