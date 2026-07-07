package com.kevinfreyap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kevinfreyap.database.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM `transaction`")
    fun getAllTransaction(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM `transaction` WHERE transactionId = :id")
    fun getTransaction(id: String): Flow<TransactionEntity>

    @Query("DELETE FROM `transaction` WHERE transactionId = :id")
    suspend fun deleteTransaction(id: String)
}