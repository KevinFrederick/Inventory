package com.kevinfreyap.product.domain.model.error

import com.kevinfreyap.domain.RootError

enum class BatchExpirationError: RootError {
    CANNOT_BE_IN_PAST
}