package com.kevinfreyap.product.presentation.state

data class BatchInformationState(
    val batchExpirationMillis: Long? = null,
    val batchExpirationFieldText: String = "",
    val batchExpirationText: String = "",
    val batchSupplier: String? = null,
)
