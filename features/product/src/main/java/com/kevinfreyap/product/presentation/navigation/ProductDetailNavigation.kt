package com.kevinfreyap.product.presentation.navigation

sealed interface ProductDetailNavigation {
    data object NavigateUp: ProductDetailNavigation
}