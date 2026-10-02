package com.kevinfreyap.auth.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface AuthScreens {
    @Serializable
    data object Onboard: AuthScreens

    @Serializable
    data object Register: AuthScreens

    @Serializable
    data object Login: AuthScreens
}