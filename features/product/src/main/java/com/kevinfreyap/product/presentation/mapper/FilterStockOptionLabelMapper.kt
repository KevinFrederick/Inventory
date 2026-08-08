package com.kevinfreyap.product.presentation.mapper

import com.kevinfreyap.product.R
import com.kevinfreyap.product.domain.model.query.FilterStockOption

fun FilterStockOption.toLabel(): Int {
    return when(this) {
        FilterStockOption.IN_STOCK -> R.string.sort_stock_in_stock
        FilterStockOption.LOW_STOCK -> R.string.sort_stock_low_stock
        FilterStockOption.OUT_OF_STOCK -> R.string.sort_stock_out_of_stock
        FilterStockOption.STOCK_WARNING -> R.string.sort_stock_warning
    }
}