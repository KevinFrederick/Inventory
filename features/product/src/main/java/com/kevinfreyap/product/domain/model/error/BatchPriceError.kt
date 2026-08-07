package com.kevinfreyap.product.domain.model.error

import com.kevinfreyap.domain.RootError

enum class BatchPriceError: RootError {
    INVALID_FORMAT,
    CANNOT_BE_NEGATIVE,
    REQUIRES_CONFIRMATION
}