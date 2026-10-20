package com.kevinfreyap.user.presentation.state

data class PendingMemberListItemState(
    val memberId: String,
    val requestName: String,
    val requestEmail: String,
    val requestDate: String
)
