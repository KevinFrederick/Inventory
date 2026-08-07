package com.kevinfreyap.product.domain.model.error

import com.kevinfreyap.domain.RootError

enum class BatchQuantityError: RootError {
    EMPTY,
    INVALID_FORMAT,
    CANNOT_BE_NEGATIVE,
    CANNOT_BE_ZERO,
    REQUIRES_CONFIRMATION
}