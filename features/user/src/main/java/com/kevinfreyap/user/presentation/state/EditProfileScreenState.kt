package com.kevinfreyap.user.presentation.state

import com.kevinfreyap.ui.state.UiState
import com.kevinfreyap.user.domain.validation.error.UserFormError

data class EditProfileScreenState(
    val originalUserDetail: UserDetailState = UserDetailState(),

    val userDetail: UserDetailState = UserDetailState(),
    val formErrors: UserFormError? = null,
    val uiState: UiState<Unit> = UiState.Idle
) {
    val hasUnsavedChanges: Boolean
        get() = originalUserDetail != userDetail

}
