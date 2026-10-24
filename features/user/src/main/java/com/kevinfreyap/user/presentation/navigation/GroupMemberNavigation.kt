package com.kevinfreyap.user.presentation.navigation

sealed interface GroupMemberNavigation {
    data object NavigateUp: GroupMemberNavigation
}