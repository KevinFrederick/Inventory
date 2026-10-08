package com.kevinfreyap.user.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface UserScreens {
    @Serializable
    data object ProfileScreen: UserScreens
}