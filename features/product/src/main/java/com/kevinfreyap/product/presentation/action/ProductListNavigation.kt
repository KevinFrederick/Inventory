package com.kevinfreyap.product.presentation.action

sealed interface ProductListNavigation {
    object AddProduct: ProductListNavigation
    data class ProductDetail(val productId: String): ProductListNavigation
}