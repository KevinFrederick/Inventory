package com.kevinfreyap.auth.presentation.navigation

sealed interface OnboardNavigation {
    object Register: OnboardNavigation
    object Login: OnboardNavigation
}