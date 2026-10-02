package com.kevinfreyap.auth.domain.error

import com.kevinfreyap.domain.RootError

enum class AuthEmailError: RootError {
    EMPTY,
    TOO_LONG,
    INVALID_FORMAT
}