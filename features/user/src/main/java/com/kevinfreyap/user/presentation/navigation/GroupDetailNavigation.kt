package com.kevinfreyap.user.presentation.navigation

sealed interface GroupDetailNavigation {
    data object NavigateUp: GroupDetailNavigation
    data class EditGroup(val groupId: String): GroupDetailNavigation
    data object BrowseInventory: GroupDetailNavigation
    data class AllMember(val groupId: String): GroupDetailNavigation
    data class MemberDetail(val memberId: String): GroupDetailNavigation
}