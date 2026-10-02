package com.kevinfreyap.auth.presentation.navigation

sealed interface LoginNavigation: AuthRoute {
    object NavigateUp: LoginNavigation
    object Register: LoginNavigation
    object Dashboard: LoginNavigation
}