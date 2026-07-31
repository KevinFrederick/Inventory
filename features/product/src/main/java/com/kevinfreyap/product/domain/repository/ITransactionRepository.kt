package com.kevinfreyap.product.domain.repository

import com.kevinfreyap.product.domain.model.Transaction
import com.kevinfreyap.product.domain.model.TransactionId
import kotlinx.coroutines.flow.Flow

interface ITransactionRepository {
    suspend fun insertTransaction(transaction: Transaction)

    fun getAllTransaction(): Flow<List<Transaction>>

    fun getTransactionById(transactionId: TransactionId): Flow<Transaction>

    suspend fun deleteTransaction(transactionId: TransactionId)
}