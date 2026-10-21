package com.kevinfreyap.user.presentation.state

data class GroupListScreenState(
    val groups: List<GroupListItemState> = emptyList(),
    val inviteCode: String = "",
    val errorMessage: String? = null
)
