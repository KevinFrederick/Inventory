package com.kevinfreyap.user.presentation.action

sealed interface GroupFormAction {
    data class OnNameChanged(val name: String) : GroupFormAction
    data class OnDescriptionChanged(val description: String) : GroupFormAction
    data class OnAddressChanged(val address: String) : GroupFormAction
    data object Submit : GroupFormAction
}