package com.kevinfreyap.product.presentation.state

import com.kevinfreyap.product.domain.model.error.ProductFormError

data class SharedBatchFormState(
    val batchDetail: BatchFormDetailState,
    val batchInformation: BatchFormInformationState,
    val formErrors: ProductFormError?
)
