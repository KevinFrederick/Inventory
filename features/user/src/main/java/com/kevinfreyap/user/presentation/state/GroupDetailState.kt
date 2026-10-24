package com.kevinfreyap.user.presentation.state

data class GroupDetailState (
    val name: String = "",
    val description: String? = null,
    val totalProducts: Int = 0,
    val totalMembers: Int = 0,
    val address: String? = null,
    val owner: String = "",
    val created: String = ""
)