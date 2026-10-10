package com.kevinfreyap.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kevinfreyap.database.model.SyncState

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
        ),
        ForeignKey(
            entity = GroupEntity::class,
            parentColumns = ["groupId"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["groupId","batchId"], unique = true),
        Index(value = ["groupId", "productId"]),
        Index(value = ["groupId", "locationId"]),
    ]
)
data class StockBatchEntity(
    @PrimaryKey(autoGenerate = false)
    val batchId: String,
    val productId: String,
    val locationId: String,
    val groupId: String,
    val quantity: Int,
    val expirationDate: Long?,
    val price: Double,
    val supplier: String?,
    val createdAt: Long,
    val lastUpdated: Long,
    val syncState: SyncState = SyncState.SYNCED
)
