package com.kevinfreyap.auth.domain.error

import com.kevinfreyap.domain.error.RootError

enum class AuthConfirmPasswordError: RootError {
    EMPTY,
    DOES_NOT_MATCH
}