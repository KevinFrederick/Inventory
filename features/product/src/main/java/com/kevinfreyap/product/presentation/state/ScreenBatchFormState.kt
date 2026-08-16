package com.kevinfreyap.product.presentation.state

import com.kevinfreyap.product.domain.model.error.ProductFormError
import com.kevinfreyap.ui.state.UiState

data class ScreenBatchFormState(
    val productName: String = "",
    val batchShortId: String? = null,

    // Initial data
    val originalBatchDetail: BatchFormDetailState = BatchFormDetailState(),
    val originalBatchInformation: BatchFormInformationState = BatchFormInformationState(),

    // Stock Batch Details
    val batchDetail: BatchFormDetailState = BatchFormDetailState(),
    val batchInformation: BatchFormInformationState = BatchFormInformationState(),

    // UI Status
    val formErrors: ProductFormError? = null,
    val uiState: UiState<Unit> = UiState.Idle,
    val showSummaryConfirmationDialog: Boolean = false,

    val isExistingBatch: Boolean = false,
    val isReadOnly: Boolean = false
) {
    val hasUnsavedChanges: Boolean
        get() = batchDetail != originalBatchDetail ||
                batchInformation != originalBatchInformation

    val isSavedEnabled: Boolean
        get() = hasUnsavedChanges &&
                batchDetail.batchQuantity.isNotBlank() &&
                batchDetail.batchQuantity != "0" &&
                batchDetail.batchLocation.isNotBlank() &&
                formErrors?.hasAnyError != true
}
