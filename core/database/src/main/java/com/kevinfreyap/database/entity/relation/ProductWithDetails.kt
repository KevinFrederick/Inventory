package com.kevinfreyap.database.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.kevinfreyap.database.entity.CategoryEntity
import com.kevinfreyap.database.entity.LocationEntity
import com.kevinfreyap.database.entity.ProductEntity

data class ProductWithDetails(
    @Embedded
    val product: ProductEntity,

    @Relation(
        parentColumn = "categoryId", // Foreign Key in ProductEntity
        entityColumn = "categoryId", // Primary Key in CategoryEntity
    )
    val category: CategoryEntity,

    @Relation(
        parentColumn = "locationId",
        entityColumn = "locationId",
    )
    val location: LocationEntity,
)