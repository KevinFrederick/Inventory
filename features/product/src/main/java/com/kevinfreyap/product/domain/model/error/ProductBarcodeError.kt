package com.kevinfreyap.product.domain.model.error

import com.kevinfreyap.domain.RootError

enum class ProductBarcodeError: RootError {
    TOO_LONG,
    INVALID_CHARACTER,
    CONTAINS_WHITESPACE,
    ALREADY_EXISTS
}