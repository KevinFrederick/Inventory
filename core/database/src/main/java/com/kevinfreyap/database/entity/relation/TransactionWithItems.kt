package com.kevinfreyap.database.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.kevinfreyap.database.entity.TransactionEntity
import com.kevinfreyap.database.entity.TransactionItemEntity

data class TransactionWithItems(
    @Embedded
    val transaction: TransactionEntity,

    @Relation(
        entity = TransactionItemEntity::class,
        parentColumn = "transactionId",
        entityColumn = "transactionId"
    )
    val items: List<TransactionWithProduct>
)
