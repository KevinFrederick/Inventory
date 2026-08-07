package com.kevinfreyap.product.domain.model.error

import com.kevinfreyap.domain.RootError

enum class BatchLocationError: RootError {
    EMPTY,
    TOO_LONG,
    CONTAINS_NEWLINE
}