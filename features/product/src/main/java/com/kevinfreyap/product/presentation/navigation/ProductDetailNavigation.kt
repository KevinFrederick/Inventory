package com.kevinfreyap.product.presentation.navigation

sealed interface ProductDetailNavigation {
    data object NavigateUp: ProductDetailNavigation
    data class EditProduct(val productId: String): ProductDetailNavigation
}