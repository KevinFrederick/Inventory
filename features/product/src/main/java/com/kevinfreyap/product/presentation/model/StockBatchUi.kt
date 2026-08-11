package com.kevinfreyap.product.presentation.model

data class StockBatchUi(
    val id: String,
    val quantity: Int,
    val price: String,
    val location: String,
    val expDate: String?,
    val supplier: String?
)
