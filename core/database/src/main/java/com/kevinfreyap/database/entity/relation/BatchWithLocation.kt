package com.kevinfreyap.database.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.kevinfreyap.database.entity.LocationEntity
import com.kevinfreyap.database.entity.StockBatchEntity

data class BatchWithLocation(
    @Embedded
    val batch: StockBatchEntity,

    @Relation (
        parentColumn = "locationId",
        entityColumn = "locationId"
    )
    val location: LocationEntity
)
