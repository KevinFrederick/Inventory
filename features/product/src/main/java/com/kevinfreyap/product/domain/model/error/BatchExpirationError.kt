package com.kevinfreyap.product.domain.model.error

import com.kevinfreyap.domain.error.RootError

enum class BatchExpirationError: RootError {
    CANNOT_BE_IN_PAST
}