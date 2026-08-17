package com.kevinfreyap.product.domain.model.query.sort

enum class SortOption (
    val columnName: String,
) {
    DATE (
        columnName = "createdAt",
    ),
    NAME (
        columnName = "name",
    ),
    QUANTITY (
        columnName = "quantity",
    ),
    PRICE (
        columnName = "price",
    ),
    LAST_UPDATED(
        columnName = "lastUpdated"
    )
}