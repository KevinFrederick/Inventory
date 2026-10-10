package com.kevinfreyap.auth.domain.error

import com.kevinfreyap.domain.error.RootError
import com.kevinfreyap.domain.error.EmailError
import com.kevinfreyap.domain.error.NetworkError

data class LoginError(
    val emailError: EmailError? = null,
    val passError: AuthPasswordError? = null,
    val networkError: NetworkError? = null
): RootError
