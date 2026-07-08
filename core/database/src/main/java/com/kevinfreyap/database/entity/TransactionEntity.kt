package com.kevinfreyap.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction")
data class TransactionEntity(
    @PrimaryKey (autoGenerate = false)
    val transactionId: String,
    val note: String?,
    val timeStamp: Long
)
