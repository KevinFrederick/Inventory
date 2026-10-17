package com.kevinfreyap.user.presentation.state

data class MemberListItemState(
    val username: String,
    val email: String,
    val role: String,
    val profilePicture: String?,
)
