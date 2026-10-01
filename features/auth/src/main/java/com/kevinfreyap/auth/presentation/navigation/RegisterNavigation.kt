package com.kevinfreyap.auth.presentation.navigation

sealed interface RegisterNavigation {
    object NavigateUp: RegisterNavigation
    object Login: RegisterNavigation
}