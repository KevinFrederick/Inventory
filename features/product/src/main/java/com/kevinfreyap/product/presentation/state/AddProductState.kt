package com.kevinfreyap.product.presentation.state

import com.kevinfreyap.product.domain.model.error.ProductFormError
import com.kevinfreyap.ui.state.UiState

data class AddProductState(
    // Product Details
    val productDetail: AddProductDetailState = AddProductDetailState(),
    val productIdentification: AddProductIdentificationState = AddProductIdentificationState(),

    // Stock Batch Details
    val batchDetail: AddBatchDetailState = AddBatchDetailState(),
    val batchInformation: AddBatchInformationState = AddBatchInformationState(),

    // UI Status
    val formErrors: ProductFormError? = null,
    val uiState: UiState<Unit> = UiState.Idle,
    val showSummaryConfirmationDialog: Boolean = false,
) {
    val hasUnsavedChanges: Boolean
        get() = productDetail.productName.isNotBlank() ||
                productDetail.productCategoryName.isNotBlank() ||
                productDetail.productDescription?.isNotBlank() == true ||
                (productDetail.productMinQuantity.isNotBlank() && productDetail.productMinQuantity != "0") ||
                productDetail.productImageUriString?.isNotBlank() == true ||
                productIdentification.productSku?.isNotBlank() == true ||
                productIdentification.productBarcode?.isNotBlank() == true ||
                (batchDetail.batchQuantity.isNotBlank() && batchDetail.batchQuantity != "0") ||
                batchDetail.batchLocation.isNotBlank() ||
                batchDetail.batchPrice.isNotBlank() ||
                batchInformation.batchExpirationText?.isNotBlank() == true ||
                batchInformation.batchSupplier?.isNotBlank() == true

    val isSavedEnabled: Boolean
        get() {
            val isProductValid = productDetail.productName.isNotBlank() &&
                                 productDetail.productCategoryName.isNotBlank() &&
                                 formErrors?.hasAnyError != true

            val isInitialStockValid = if (batchDetail.addInitialStock) {
                batchDetail.batchQuantity.isNotBlank() && batchDetail.batchQuantity != "0" &&
                batchDetail.batchLocation.isNotBlank()
            } else {
                true
            }

            return isProductValid && isInitialStockValid
        }
}
