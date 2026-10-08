package com.kevinfreyap.user.presentation.state

data class ProfileScreenState(
    val userDetail: UserDetailState,
    val totalGroups: Int,
    val groups: List<GroupListItemState>
)
