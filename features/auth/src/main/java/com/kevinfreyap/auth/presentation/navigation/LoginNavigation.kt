package com.kevinfreyap.auth.presentation.navigation

sealed interface LoginNavigation {
    object NavigateUp: LoginNavigation
    object Register: LoginNavigation
}