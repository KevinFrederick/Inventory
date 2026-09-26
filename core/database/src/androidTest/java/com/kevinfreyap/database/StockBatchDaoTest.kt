package com.kevinfreyap.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kevinfreyap.database.dao.BatchDao
import com.kevinfreyap.database.dao.ProductDao
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StockBatchDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var productDao: ProductDao
    private lateinit var stockBatchDao: BatchDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        productDao = database.productDao()
        stockBatchDao = database.batchDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun cascadeDelete_whenProductDeleted_stockIsAlsoDeleted() = runTest {
        database.populateWithData()

        // Initial batches
        val initialBatches = stockBatchDao.getBatchesByLocation("loc-01").first()
        assertEquals(4, initialBatches.size)

        // Delete the Product
        productDao.deleteProduct("prod-01")

        // Verify the cascade delete worked
        val remainingBatches = stockBatchDao.getBatchesByLocation("loc-01").first()
        assertEquals(2, remainingBatches.size)
    }

    @Test
    fun fetchProduct_returnsProductWithAllRelatedBatchesAndLocations() = runTest {
        database.populateWithData()

        val productWithDetails = requireNotNull(productDao.getProduct("prod-03").first()) {
            "Product prod-03 should exist in the database but returned null"
        }

        // Assert Room stitched it all together correctly
        assertEquals("Milk", productWithDetails.product.name)
        assertEquals("Food", productWithDetails.category.name)
        assertEquals(2, productWithDetails.batches.size) // Proves one-to-many works

        // Check that the nested locations mapped correctly
        val locations = productWithDetails.batches.map { it.location.name }
        assertTrue(locations.contains("Kitchen"))
        assertTrue(locations.contains("Garage"))
    }
}