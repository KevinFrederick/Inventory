package com.kevinfreyap.user.presentation.state


data class GroupListItemState(
    val isActive: Boolean,
    val groupId: String,
    val name: String,
    val totalProduct: Int,
    val totalMember: Int
)
