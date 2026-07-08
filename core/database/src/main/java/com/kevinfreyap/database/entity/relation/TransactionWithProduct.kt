package com.kevinfreyap.database.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.kevinfreyap.database.entity.ProductEntity
import com.kevinfreyap.database.entity.TransactionItemEntity

data class TransactionWithProduct(
    @Embedded
    val transactionItem: TransactionItemEntity,

    @Relation(
        parentColumn = "productId",
        entityColumn = "productId"
    )
    val product: ProductEntity
)
