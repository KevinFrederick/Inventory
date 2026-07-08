package com.kevinfreyap.product.domain.model

data class Transaction(
    val transactionId: String,
    val product: Product,
    val note: String?,
    val amount: Int,
    val timeStamp: Long
)
