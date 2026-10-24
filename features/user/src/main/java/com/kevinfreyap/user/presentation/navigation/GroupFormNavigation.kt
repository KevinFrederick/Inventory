package com.kevinfreyap.user.presentation.navigation

sealed interface GroupFormNavigation {
    data object NavigateUp : GroupFormNavigation
}