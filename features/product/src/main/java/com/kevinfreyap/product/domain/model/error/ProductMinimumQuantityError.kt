package com.kevinfreyap.product.domain.model.error

import com.kevinfreyap.domain.error.RootError

enum class ProductMinimumQuantityError: RootError {
    INVALID_FORMAT,
    CANNOT_BE_NEGATIVE,
    REQUIRES_CONFIRMATION
}