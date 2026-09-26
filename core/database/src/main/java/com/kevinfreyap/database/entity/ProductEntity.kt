package com.kevinfreyap.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kevinfreyap.database.model.SyncState

@Entity (
    tableName = "product",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.RESTRICT
        ),
    ],
    indices = [
        Index(value = ["categoryId"]),
        Index(value = ["sku"], unique = true),
        Index(value = ["barcode"], unique = true)
    ]
)
data class ProductEntity (
    @PrimaryKey (autoGenerate = false)
    val productId: String,
    val categoryId: String,
    val name: String,
    val description: String?,
    val barcode: String?,
    val barcodeFormat: String?,
    val sku: String?,
    val imageUri: String?,
    val minimumQuantity: Int,
    val createdAt: Long,
    val lastUpdated: Long,
    val syncState: SyncState = SyncState.SYNCED
)