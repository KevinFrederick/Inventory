package com.kevinfreyap.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "stock_batch",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["productId"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LocationEntity::class,
            parentColumns = ["locationId"],
            childColumns = ["locationId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["productId"]),
        Index(value = ["locationId"])
    ]
)
data class StockBatchEntity(
    @PrimaryKey(autoGenerate = false)
    val batchId: String,
    val productId: String,
    val locationId: String,
    val quantity: Int,
    val expirationDate: Long?,
    val price: Double,
    val supplier: String?,
    val lastUpdated: Long
)
