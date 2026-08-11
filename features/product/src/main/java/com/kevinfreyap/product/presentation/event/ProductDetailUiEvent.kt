package com.kevinfreyap.product.presentation.event

import com.kevinfreyap.product.presentation.navigation.ProductDetailNavigation

sealed interface ProductDetailUiEvent {
    data class Navigate (val destination: ProductDetailNavigation): ProductDetailUiEvent
    data class ShowToast(val messageRes: Int): ProductDetailUiEvent
}