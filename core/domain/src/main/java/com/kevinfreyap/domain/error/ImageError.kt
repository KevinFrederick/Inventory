package com.kevinfreyap.domain.error

import com.kevinfreyap.domain.RootError

enum class ImageError: RootError {
    INVALID_FORMAT,
    PATH_TOO_LONG,
    FILE_TOO_LARGE
}