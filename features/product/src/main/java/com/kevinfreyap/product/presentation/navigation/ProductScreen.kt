package com.kevinfreyap.product.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface ProductScreen {
    @Serializable
    data object Dashboard: ProductScreen

    @Serializable
    data object AddProduct: ProductScreen

    @Serializable
    data class ProductList (val stockFilter: String? = null): ProductScreen

    @Serializable
    data class ProductDetail(val productId: String): ProductScreen
}