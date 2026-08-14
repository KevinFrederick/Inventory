package com.kevinfreyap.product.presentation.action

sealed interface ProductDetailAction {
    data object OnEditButtonClick: ProductDetailAction
    data object OnDeleteButtonClick: ProductDetailAction
    data object OnConfirmDelete: ProductDetailAction
    data object OnCancelDelete: ProductDetailAction
}