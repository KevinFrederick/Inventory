package com.kevinfreyap.auth.presentation.state

import com.kevinfreyap.auth.domain.error.AuthConfirmPasswordError
import com.kevinfreyap.auth.domain.error.AuthEmailError
import com.kevinfreyap.auth.domain.error.AuthNameError
import com.kevinfreyap.auth.domain.error.AuthPasswordError
import com.kevinfreyap.ui.state.UiState

data class ScreenAuthState (
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val resetEmail: String = "",
    val nameError: AuthNameError? = null,
    val emailError: AuthEmailError? = null,
    val passwordError: AuthPasswordError? = null,
    val confirmError: AuthConfirmPasswordError? = null,
    val resetEmailError: AuthEmailError? = null,

    val uiState: UiState<Unit> = UiState.Idle
)