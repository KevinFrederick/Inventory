package com.kevinfreyap.product.presentation.navigation

sealed interface ProductDetailNavigation {
    data object NavigateUp: ProductDetailNavigation
    data class EditProduct(val productId: String): ProductDetailNavigation
    data class AddBatch(val productId: String): ProductDetailNavigation
    data class BatchDetail(
        val productId: String,
        val batchId: String
    ): ProductDetailNavigation
}