package com.kevinfreyap.user.presentation.action

interface GroupListAction {
    data class OnInviteCodeChanged(val code: String) : GroupListAction
    data object OnJoinGroupClick : GroupListAction
    data object OnRefresh : GroupListAction
}