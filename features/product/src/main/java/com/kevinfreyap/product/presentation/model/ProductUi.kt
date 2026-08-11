package com.kevinfreyap.product.presentation.model

data class ProductUi(
    val id: String,
    val imageUri: String?,
    val name: String,
    val category: String,
    val sku: String?,
    val description: String?,
    val totalQty: String,
    val batches: List<StockBatchUi>?,
    val barcode: String?
)