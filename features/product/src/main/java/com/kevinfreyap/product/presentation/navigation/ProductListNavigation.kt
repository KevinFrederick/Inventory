package com.kevinfreyap.product.presentation.navigation

sealed interface ProductListNavigation {
    object AddProduct: ProductListNavigation
    data class ProductDetail(val productId: String): ProductListNavigation
}