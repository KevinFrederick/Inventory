package com.kevinfreyap.auth.presentation.navigation

sealed interface RegisterNavigation: AuthRoute {
    object NavigateUp: RegisterNavigation
    object Login: RegisterNavigation
    object Dashboard: RegisterNavigation
}