package com.kevinfreyap.product.presentation.state

data class AddBatchInformationState(
    val batchExpirationMillis: Long? = null,
    val batchExpirationFieldText: String = "",
    val batchExpirationText: String? = null,
    val batchSupplier: String? = null,
)
