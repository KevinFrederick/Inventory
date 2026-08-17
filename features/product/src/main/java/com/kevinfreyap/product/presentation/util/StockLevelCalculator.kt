package com.kevinfreyap.product.presentation.util

import com.kevinfreyap.product.presentation.model.StockLevel

object StockLevelCalculator {
    fun calculateStockLevel(qty: Int, minQty: Int): StockLevel {
        return when {
            qty <= 0 -> StockLevel.OUT_OF_STOCK
            qty <= minQty -> StockLevel.LOW_STOCK
            else -> StockLevel.IN_STOCK
        }
    }
}