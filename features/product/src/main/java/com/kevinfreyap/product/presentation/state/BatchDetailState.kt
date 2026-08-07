package com.kevinfreyap.product.presentation.state

data class BatchDetailState(
    val addInitialStock: Boolean = false,
    val batchQuantity: String = "0",
    val isQuantityConfirmed: Boolean = false,
    val batchPrice: String = "",
    val isPriceConfirmed:  Boolean = false,
    val batchLocation: String = "",
    val allLocations: List<String> = emptyList(),
    val filteredLocations: List<String> = emptyList()
)
