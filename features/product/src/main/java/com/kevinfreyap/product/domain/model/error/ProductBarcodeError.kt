package com.kevinfreyap.product.domain.model.error

import com.kevinfreyap.domain.error.RootError

enum class ProductBarcodeError: RootError {
    TOO_LONG,
    INVALID_CHARACTER,
    CONTAINS_WHITESPACE,
    ALREADY_EXISTS
}