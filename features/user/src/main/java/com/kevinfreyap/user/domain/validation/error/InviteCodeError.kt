package com.kevinfreyap.user.domain.validation.error

import com.kevinfreyap.domain.error.NetworkError
import com.kevinfreyap.domain.error.RootError

enum class CodeError: RootError {
    EMPTY
}

data class InviteCodeError (
    val fieldError: CodeError? = null,
    val networkError: NetworkError? = null
): RootError