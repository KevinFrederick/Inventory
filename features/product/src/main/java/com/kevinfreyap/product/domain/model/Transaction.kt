package com.kevinfreyap.product.domain.model

@JvmInline
value class TransactionId (val value: String)

data class Transaction(
    val transactionId: TransactionId,
    val note: String?,
    val items: List<TransactionItem>,
    val timeStamp: Long
)
