package com.kevinfreyap.product.presentation.state

import com.kevinfreyap.product.domain.model.error.ProductFormError

data class SharedProductFormState(
    val productDetail: ProductFormDetailState,
    val productIdentification: ProductFormIdentificationState,
    val formErrors: ProductFormError?
)
