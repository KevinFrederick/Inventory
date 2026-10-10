package com.kevinfreyap.product.domain.model.error

import com.kevinfreyap.domain.error.RootError

enum class ProductNameError: RootError {
    EMPTY,
    TOO_LONG,
    CONTAINS_NEWLINE
}