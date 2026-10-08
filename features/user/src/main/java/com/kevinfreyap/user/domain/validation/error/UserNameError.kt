package com.kevinfreyap.user.domain.validation.error

import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.RootError
import com.kevinfreyap.domain.util.cleanInlineSpaces

enum class UserNameError: RootError {
    EMPTY,
    TOO_LONG,
    CONTAINS_NEWLINE
}