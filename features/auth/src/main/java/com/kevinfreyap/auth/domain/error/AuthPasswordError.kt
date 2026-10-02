package com.kevinfreyap.auth.domain.error

import com.kevinfreyap.domain.RootError

enum class AuthPasswordError: RootError {
    EMPTY,
    TOO_SHORT,
    TOO_LONG,
    NO_UPPERCASE,
    NO_DIGIT
}