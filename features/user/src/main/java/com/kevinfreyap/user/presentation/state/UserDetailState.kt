package com.kevinfreyap.user.presentation.state

data class UserDetailState(
    val name: String,
    val email: String,
    val profileUrl: String?,
    val phoneNumber: String?,
    val jobTitle: String?
)
