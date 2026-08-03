package com.kevinfreyap.database

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kevinfreyap.database.TestData.category1
import com.kevinfreyap.database.TestData.prod1
import com.kevinfreyap.database.dao.CategoryDao
import com.kevinfreyap.database.dao.TransactionDao
import com.kevinfreyap.database.entity.TransactionEntity
import com.kevinfreyap.database.entity.TransactionItemEntity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseRelationTest {
    private lateinit var database: AppDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var transactionDao: TransactionDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()

        categoryDao = database.categoryDao()
        transactionDao = database.transactionDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    val transaction = TransactionEntity(
        transactionId = "trans-01",
        note = "test",
        timeStamp = 1000L
    )

    val transactionItem = TransactionItemEntity(
        transactionItemId = "item-01",
        transactionId = "trans-01",
        batchId = "batch-01",
        amount = 2,
    )

    @Test
    fun deleteCategory_withLinkedProduct_isRestricted() = runTest{
        // Insert a category and product linked to it
        database.populateWithData()

        // Catch whatever happens when deleting
        val result = runCatching {
            categoryDao.deleteCategory(category1.categoryId)
        }

        // Assert | Because ForeignKey.Restrict, deleting the category must crash
        val exception = result.exceptionOrNull()
        assertTrue(exception is SQLiteConstraintException)
    }

    @Test
    fun insertAndRetrieveTransaction_withItems() = runTest {
        database.populateWithData()

        transactionDao.insertTransaction(transaction)
        transactionDao.insertTransactionItems(listOf(transactionItem))

        // Fetch data
        val result = transactionDao.getTransaction("trans-01")

        // Assert relations correct
        assertEquals(transaction.transactionId, result.first().transaction.transactionId)
        assertEquals(1, result.first().items.size)
        assertEquals(prod1.name, result.first().items.first().batchDetails.product.name)
    }
}