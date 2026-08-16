package com.kevinfreyap.product.presentation.action

sealed interface ProductDetailAction {
    data object OnEditProductClick: ProductDetailAction
    data object OnAddBatchClick: ProductDetailAction
    data class OnBatchDetailClick(val batchId: String): ProductDetailAction
    data object OnDeleteButtonClick: ProductDetailAction
    data object OnConfirmDelete: ProductDetailAction
    data object OnCancelDelete: ProductDetailAction
}