package com.kevinfreyap.product.presentation.navigation

import kotlinx.serialization.Serializable

sealed class ProductScreen {
    @Serializable
    data class ProductDetailRoute(val productId: String)
}