package com.kevinfreyap.auth.domain.error

import com.kevinfreyap.domain.RootError

enum class AuthNameError: RootError {
    EMPTY,
    TOO_LONG,
    CONTAINS_NEWLINE
}