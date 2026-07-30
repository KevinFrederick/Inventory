package com.kevinfreyap.product.domain.model

@JvmInline
value class TransactionItemId(val value: String)

data class TransactionItem(
    val transactionItemId: TransactionItemId,
    val product: Product,
    val amount: Int
)
