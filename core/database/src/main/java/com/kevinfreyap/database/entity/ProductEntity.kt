package com.kevinfreyap.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity (
    tableName = "product",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = LocationEntity::class,
            parentColumns = ["locationId"],
            childColumns = ["locationId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index(value = ["categoryId"]), Index(value = ["locationId"])]
)
data class ProductEntity (
    @PrimaryKey (autoGenerate = false)
    val productId: String,
    val categoryId: String,
    val locationId: String,
    val name: String,
    val description: String?,
    val barcode: String,
    val sku: String,
    val quantity: Int,
    val price: Double,
    val imageUri: String?,
    val expirationDate: Long?,
    val minimumQuantity: Int,
    val supplier: String?,
    val createdAt: Long,
    val lastUpdated: Long
)