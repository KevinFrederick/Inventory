package com.kevinfreyap.domain.error

import com.kevinfreyap.domain.RootError

enum class EmailError: RootError {
    EMPTY,
    TOO_LONG,
    INVALID_FORMAT
}