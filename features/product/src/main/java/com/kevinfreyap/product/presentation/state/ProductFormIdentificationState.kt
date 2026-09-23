package com.kevinfreyap.product.presentation.state

data class ProductFormIdentificationState(
    val productBarcode: String? = null,
    val productBarcodeFormat: String? = null,
    val productSku: String? = null,
)
