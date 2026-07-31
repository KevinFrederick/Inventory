package com.kevinfreyap.database.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.kevinfreyap.database.entity.LocationEntity
import com.kevinfreyap.database.entity.ProductEntity
import com.kevinfreyap.database.entity.StockBatchEntity

data class BatchWithProductAndLocation(
    @Embedded
    val batch: StockBatchEntity,

    @Relation (
        parentColumn = "productId",
        entityColumn = "productId"
    )
    val product: ProductEntity,

    @Relation(
        parentColumn = "locationId",
        entityColumn = "locationId"
    )
    val location: LocationEntity
)
