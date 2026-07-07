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
        )
    ],
    indices = [Index(value = ["categoryId"])]
)
data class ProductEntity (
    @PrimaryKey (autoGenerate = false)
    val productId: String,
    val categoryId: String,
    val name: String,
    val description: String?,
    val sku: String,
    val quantity: Int,
    val price: Double,
    val minimumQuantity: Int,
    val createdAt: Long,
    val lastUpdated: Long
)