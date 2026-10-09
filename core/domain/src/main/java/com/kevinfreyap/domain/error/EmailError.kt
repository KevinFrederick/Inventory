package com.kevinfreyap.domain.error

enum class EmailError: RootError {
    EMPTY,
    TOO_LONG,
    INVALID_FORMAT
}