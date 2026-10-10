package com.kevinfreyap.user.domain.validation.error

import com.kevinfreyap.domain.error.RootError

enum class UserNameError: RootError {
    EMPTY,
    TOO_LONG,
    CONTAINS_NEWLINE
}