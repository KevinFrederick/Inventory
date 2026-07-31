package com.kevinfreyap.database.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.kevinfreyap.database.entity.ProductEntity
import com.kevinfreyap.database.entity.StockBatchEntity
import com.kevinfreyap.database.entity.TransactionItemEntity

data class TransactionItemWithDetails(
    // 1 line inside a receipt
    @Embedded
    val transactionItem: TransactionItemEntity,

    @Relation(
        entity = StockBatchEntity::class,
        parentColumn = "batchId",
        entityColumn = "batchId"
    )
    val batchDetails: BatchWithProduct
)
