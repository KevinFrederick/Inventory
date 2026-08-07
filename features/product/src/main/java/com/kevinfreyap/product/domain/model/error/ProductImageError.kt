package com.kevinfreyap.product.domain.model.error

import com.kevinfreyap.domain.RootError

enum class ProductImageError: RootError {
    INVALID_FORMAT,
    PATH_TOO_LONG,
    FILE_TOO_LARGE
}