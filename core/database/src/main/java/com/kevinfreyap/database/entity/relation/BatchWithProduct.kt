package com.kevinfreyap.database.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.kevinfreyap.database.entity.ProductEntity
import com.kevinfreyap.database.entity.StockBatchEntity

data class BatchWithProduct(
    @Embedded
    val batch: StockBatchEntity,

    @Relation(
        parentColumn = "productId",
        entityColumn = "productId"
    )
    val product: ProductEntity
)
