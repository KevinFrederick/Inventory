package com.kevinfreyap.user.presentation.navigation

sealed interface GroupDetailNavigation {
    data object NavigateUp: GroupDetailNavigation
}