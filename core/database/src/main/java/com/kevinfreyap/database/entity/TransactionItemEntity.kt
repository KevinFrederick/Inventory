package com.kevinfreyap.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transaction_item",
    foreignKeys = [
        ForeignKey(
            entity = TransactionEntity::class,
            parentColumns = ["transactionId"],
            childColumns = ["transactionId"],
            onDelete = ForeignKey.CASCADE // If transaction deleted, delete transaction_item too
        ),
        ForeignKey(
            entity = StockBatchEntity::class,
            parentColumns = ["batchId"],
            childColumns = ["batchId"],
            onDelete = ForeignKey.RESTRICT // Can't delete product, if still in transaction_item
        )
    ],
    indices = [
        Index("transactionId"),
        Index("batchId")
    ]
)
data class TransactionItemEntity(
    @PrimaryKey(autoGenerate = false)
    val transactionItemId: String,
    val transactionId: String,
    val batchId: String,
    val amount: Int
)
