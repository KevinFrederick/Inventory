package com.kevinfreyap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.kevinfreyap.database.entity.TransactionEntity
import com.kevinfreyap.database.entity.TransactionItemEntity
import com.kevinfreyap.database.entity.relation.TransactionWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactionItems(items: List<TransactionItemEntity>)

    @Transaction
    @Query("SELECT * FROM `transaction` ORDER BY timeStamp DESC")
    fun getAllTransactionWithItems(): Flow<List<TransactionWithItems>>

    @Query("SELECT * FROM `transaction` WHERE transactionId = :id")
    fun getTransaction(id: String): Flow<TransactionWithItems>

    @Query("DELETE FROM `transaction` WHERE transactionId = :id")
    suspend fun deleteTransaction(id: String)
}