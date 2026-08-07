package com.kevinfreyap.product.domain.model.error

import com.kevinfreyap.domain.RootError

enum class ProductSkuError: RootError {
    TOO_LONG,
    CONTAINS_WHITESPACE,
    INVALID_CHARACTERS,
    ALREADY_EXISTS
}