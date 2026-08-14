package com.kevinfreyap.product.presentation.state

import com.kevinfreyap.product.presentation.mapper.toLabel
import com.kevinfreyap.product.presentation.model.StockBatchUi
import com.kevinfreyap.product.presentation.util.StockLevelCalculator.calculateStockLevel
import com.kevinfreyap.product.presentation.util.toFormattedNumber

data class ScreenProductDetailState(
    val imageUri: String? = null,
    val productName: String = "",
    val productCategory: String = "",
    val productSku: String? = null,
    val productTotalQty: Int = 0,
    val productMinPrice: String? = null,
    val productMaxPrice: String? = null,
    val productMinQty: Int = 0,
    val productNearestExpDate: String? = null,
    val productDescription: String? = null,
    val productBarcode: String? = null,
    val batchesPreviewList: List<StockBatchUi> = emptyList(),
    val batchesList: List<StockBatchUi> = emptyList(),
    val totalBatchCount: Int = 0
) {
    val stockStatus: Int
        get() = calculateStockLevel(
            qty = productTotalQty,
            minQty = productMinQty
        ).toLabel()

    val productTotalQtyText: String
        get() = productTotalQty.toFormattedNumber()

    val productMinQtyText: String
        get() = productMinQty.toFormattedNumber()
}
