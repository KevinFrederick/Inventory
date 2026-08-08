package com.kevinfreyap.product.presentation.mapper

import com.kevinfreyap.product.R
import com.kevinfreyap.product.domain.model.query.sort.SortOption

fun SortOption.toLabel(): Int {
    return when(this) {
        SortOption.DATE -> R.string.sort_option_date
        SortOption.NAME -> R.string.sort_option_product_name
        SortOption.QUANTITY -> R.string.sort_option_quantity
        SortOption.PRICE -> R.string.sort_option_price
    }
}