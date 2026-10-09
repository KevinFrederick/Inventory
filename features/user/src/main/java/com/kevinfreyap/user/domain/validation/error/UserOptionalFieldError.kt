package com.kevinfreyap.user.domain.validation.error

import com.kevinfreyap.domain.error.RootError

enum class UserOptionalFieldError: RootError {
    TOO_LONG,
    CONTAINS_NEWLINE
}