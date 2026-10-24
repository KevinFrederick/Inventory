package com.kevinfreyap.user.presentation.navigation

sealed interface GroupListNavigation {
    data object NavigateUp: GroupListNavigation
    data object AddGroup: GroupListNavigation
    data class GroupDetail(val groupId: String): GroupListNavigation
}