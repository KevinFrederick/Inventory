package com.kevinfreyap.product.data.repository

import com.kevinfreyap.database.dao.TransactionDao
import com.kevinfreyap.product.domain.model.Transaction
import com.kevinfreyap.product.domain.model.TransactionId
import com.kevinfreyap.product.domain.repository.ITransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao
): ITransactionRepository {
    override suspend fun insertTransaction(transaction: Transaction) {
        TODO("Not yet implemented")
    }

    override fun getAllTransaction(): Flow<List<Transaction>> {
        TODO("Not yet implemented")
    }

    override fun getTransactionById(transactionId: TransactionId): Flow<Transaction> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTransaction(transactionId: TransactionId) {
        TODO("Not yet implemented")
    }
}