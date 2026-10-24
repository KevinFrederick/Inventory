package com.kevinfreyap.user.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface UserScreens {
    @Serializable
    data object ProfileScreen: UserScreens

    @Serializable
    data object  EditProfileScreen: UserScreens

    @Serializable
    data class GroupDetailScreen(val groupId: String): UserScreens

    @Serializable
    data class GroupMembersScreen(val groupId: String): UserScreens

    @Serializable
    data object GroupListScreen: UserScreens

    @Serializable
    data class GroupFormScreen(val groupId: String? = null): UserScreens
}