package com.kevinfreyap.product.presentation.state

data class AddProductDetailState(
    val productImageUriString: String? = null,
    val productName: String = "",
    val productCategoryName: String = "",
    val allCategories: List<String> = emptyList(),
    val filteredCategories: List<String> = emptyList(),
    val productDescription: String? = null,
    val productMinQuantity: String = "0",
    val isMinQuantityConfirmed: Boolean = false,
)
