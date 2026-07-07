package com.kevinfreyap.database

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kevinfreyap.database.dao.CategoryDao
import com.kevinfreyap.database.dao.ProductDao
import com.kevinfreyap.database.dao.TransactionDao
import com.kevinfreyap.database.entity.CategoryEntity
import com.kevinfreyap.database.entity.ProductEntity
import com.kevinfreyap.database.entity.TransactionEntity
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
    private lateinit var db: AppDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var productDao: ProductDao
    private lateinit var transactionDao: TransactionDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()

        categoryDao = db.categoryDao()
        productDao = db.productDao()
        transactionDao = db.transactionDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    val category = CategoryEntity(
        categoryId = "cat-01",
        name = "Food",
        description = null,
        createdAt = System.currentTimeMillis(),
        lastUpdated = System.currentTimeMillis()
    )

    val product = ProductEntity(
        productId = "prod-01",
        categoryId = "cat-01",
        name = "Burger",
        description = null,
        sku = "bgr-01",
        quantity = 10,
        price = 99.0,
        minimumQuantity = 2,
        createdAt = 1000L,
        lastUpdated = 1000L
    )

    val transaction = TransactionEntity(
        transactionId = "trans-01",
        productId = "prod-01",
        note = "restock",
        amount = 100,
        timeStamp = 1000L
    )

    @Test
    fun deleteCategory_withLinkedProduct_isRestricted() = runTest{
        // Insert a category and product linked to it
        categoryDao.insertCategory(category)
        productDao.insertProduct(product)

        // Catch whatever happens when deleting
        val result = runCatching {
            categoryDao.deleteCategory(category.categoryId)
        }

        // Assert | Because ForeignKey.Restrict, deleting the category must crash
        val exception = result.exceptionOrNull()
        assertTrue(exception is SQLiteConstraintException)
    }

    @Test
    fun deleteProduct_cascadesToTransactions() = runTest {
        categoryDao.insertCategory(category)
        productDao.insertProduct(product)
        transactionDao.insertTransaction(transaction)

        // Verify transaction exist
        var savedTransaction = transactionDao.getAllTransaction().first()
        assertEquals(1, savedTransaction.size)

        // Delete product
        val result = runCatching {
            productDao.deleteProduct(product.productId)
        }

        // Ensure product deletion didn't crash
        assertTrue(result.isSuccess)

        // Transaction must be gone
        savedTransaction = transactionDao.getAllTransaction().first()
        assertTrue(savedTransaction.isEmpty())
    }
}