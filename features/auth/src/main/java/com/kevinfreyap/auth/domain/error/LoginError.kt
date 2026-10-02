package com.kevinfreyap.auth.domain.error

import com.kevinfreyap.domain.RootError
import com.kevinfreyap.network.error.NetworkError

data class LoginError(
    val emailError: AuthEmailError? = null,
    val passError: AuthPasswordError? = null,
    val networkError: NetworkError? = null
): RootError
