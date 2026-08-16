package com.kevinfreyap.product.presentation.state

import com.kevinfreyap.product.presentation.util.DateFormatter.formatDatePickerDate

data class BatchFormInformationState(
    val batchExpirationMillis: Long? = null,
    val batchExpirationFieldText: String = "",
    val batchSupplier: String? = null,
) {
    val batchExpirationTextFormatted: String?
        get() = batchExpirationMillis?.let {
            formatDatePickerDate(it)
        }
}
