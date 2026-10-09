package com.kevinfreyap.domain.error

enum class ImageError: RootError {
    INVALID_FORMAT,
    PATH_TOO_LONG,
    FILE_TOO_LARGE
}