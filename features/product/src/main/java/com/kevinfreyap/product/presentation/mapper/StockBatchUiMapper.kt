package com.kevinfreyap.product.presentation.mapper

import com.kevinfreyap.product.domain.model.StockBatch
import com.kevinfreyap.product.presentation.model.StockBatchUi
import com.kevinfreyap.product.presentation.util.DateFormatter.formatDatePickerDate
import com.kevinfreyap.product.presentation.util.toFormattedCurrency

fun StockBatch.toUiModel(): StockBatchUi {
    val shortId = this.batchId.value.substring(6, 12).uppercase()

    return StockBatchUi(
        id = this.batchId.value,
        shortId = shortId,
        quantity = this.quantity,
        price = this.price.toFormattedCurrency(),
        location = this.location.name,
        expDate = formatDatePickerDate(this.expirationDate),
        supplier = this.supplier
    )
}

fun List<StockBatch>.toUiModel(): List<StockBatchUi> {
    return this.map { stockBatch ->
        stockBatch.toUiModel()
    }
}