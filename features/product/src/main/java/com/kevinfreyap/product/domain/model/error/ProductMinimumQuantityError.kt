package com.kevinfreyap.product.domain.model.error

import com.kevinfreyap.domain.RootError

enum class ProductMinimumQuantityError: RootError {
    INVALID_FORMAT,
    CANNOT_BE_NEGATIVE,
    REQUIRES_CONFIRMATION
}