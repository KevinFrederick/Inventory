package com.kevinfreyap.user.presentation.state

data class UserDetailState(
    val name: String = "",
    val email: String = "",
    val profileUrl: String? = null,
    val phoneNumber: String? = null,
    val jobTitle: String? = null
)
