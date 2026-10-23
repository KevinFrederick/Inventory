package com.kevinfreyap.user.presentation.state

import com.kevinfreyap.ui.state.UiState
import com.kevinfreyap.user.domain.validation.error.GroupFormError

data class GroupFormScreenState(
    val groupId: String? = null,
    val isEditMode: Boolean = false,
    val originalGroupDetail: GroupFormDetailState = GroupFormDetailState(),
    val groupDetail: GroupFormDetailState = GroupFormDetailState(),
    val formErrors: GroupFormError? = null,
    val uiState: UiState<Unit> = UiState.Idle
) {
    val hasUnsavedChanges: Boolean
        get() = originalGroupDetail != groupDetail
}