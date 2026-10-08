package com.kevinfreyap.user.domain.validation.error

import com.kevinfreyap.domain.RootError
import com.kevinfreyap.domain.error.EmailError
import com.kevinfreyap.network.error.NetworkError

data class MemberFormError(
    val emailError: EmailError? = null,
    val networkError: NetworkError? = null
): RootError
