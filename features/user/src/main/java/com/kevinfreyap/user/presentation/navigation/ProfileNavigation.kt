package com.kevinfreyap.user.presentation.navigation

sealed interface ProfileNavigation {
    data object EditProfile: ProfileNavigation
    data object ManageGroups: ProfileNavigation
    data object AddGroup: ProfileNavigation
    data class GroupDetail(val groupId: String): ProfileNavigation
}