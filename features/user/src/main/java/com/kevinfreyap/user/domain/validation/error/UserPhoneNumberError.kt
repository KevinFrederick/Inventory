package com.kevinfreyap.user.domain.validation.error

import com.kevinfreyap.domain.error.RootError

enum class UserPhoneNumberError: RootError {
    TOO_LONG,
    INVALID_FORMAT
}