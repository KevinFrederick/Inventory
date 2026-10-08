package com.kevinfreyap.user.domain.validation.error

import com.kevinfreyap.domain.RootError

enum class UserPhoneNumberError: RootError {
    TOO_LONG,
    INVALID_FORMAT
}