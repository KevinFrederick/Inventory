package com.kevinfreyap.product.domain.model.query.sort

import androidx.annotation.StringRes
import com.kevinfreyap.product.R

enum class SortOption (
    val columnName: String,
    @param:StringRes val displayName: Int,
) {
    DATE (
        columnName = "createdAt",
        displayName = R.string.sort_option_date
    ),
    NAME (
        columnName = "name",
        displayName = R.string.sort_option_product_name
    ),
    QUANTITY (
        columnName = "quantity",
        displayName = R.string.sort_option_quantity
    ),
    PRICE (
        columnName = "price",
        displayName = R.string.sort_option_price
    )
}