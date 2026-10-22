package com.kevinfreyap.user.presentation.state

data class GroupListScreenState(
    val activeGroups: List<GroupListItemState> = emptyList(),
    val pendingGroups: List<GroupListItemState> = emptyList(),
    val inviteCode: String = "",
    val errorMessage: String? = null
)
