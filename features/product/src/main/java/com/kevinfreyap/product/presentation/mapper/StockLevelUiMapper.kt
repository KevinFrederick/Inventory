package com.kevinfreyap.product.presentation.mapper

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.kevinfreyap.product.R
import com.kevinfreyap.product.presentation.model.StockLevel
import com.kevinfreyap.ui.theme.Theme

val StockLevel.color: Color
    @Composable
    get() = when(this) {
        StockLevel.IN_STOCK -> Theme.custom.success
        StockLevel.LOW_STOCK -> Theme.custom.warning
        StockLevel.OUT_OF_STOCK -> MaterialTheme.colorScheme.error
    }

fun StockLevel.toLabel(): Int {
    return when(this) {
        StockLevel.IN_STOCK -> R.string.label_in_stock
        StockLevel.LOW_STOCK -> R.string.label_low_stock
        StockLevel.OUT_OF_STOCK -> R.string.label_no_stock
    }
}