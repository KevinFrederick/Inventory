package com.kevinfreyap.auth.domain.error

import com.kevinfreyap.domain.RootError
import com.kevinfreyap.domain.error.EmailError
import com.kevinfreyap.network.error.NetworkError

data class RegisterError (
    val nameError: AuthNameError? = null,
    val emailError: EmailError? = null,
    val passError: AuthPasswordError? = null,
    val confirmPassError: AuthConfirmPasswordError? = null,
    val networkError: NetworkError? = null
): RootError