package com.kevinfreyap.user.presentation.navigation

sealed interface EditProfileNavigation {
    data object NavigateUp: EditProfileNavigation
}