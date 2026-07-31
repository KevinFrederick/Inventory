package com.kevinfreyap.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kevinfreyap.database.dao.BatchDao
import com.kevinfreyap.database.dao.CategoryDao
import com.kevinfreyap.database.dao.LocationDao
import com.kevinfreyap.database.dao.ProductDao
import com.kevinfreyap.database.entity.CategoryEntity
import com.kevinfreyap.database.entity.LocationEntity
import com.kevinfreyap.database.entity.ProductEntity
import com.kevinfreyap.database.entity.StockBatchEntity
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
    private lateinit var categoryDao: CategoryDao
    private lateinit var locationDao: LocationDao
    private lateinit var stockBatchDao: BatchDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        locationDao = database.locationDao()
        categoryDao = database.categoryDao()
        productDao = database.productDao()
        stockBatchDao = database.batchDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun cascadeDelete_whenProductDeleted_stockIsAlsoDeleted() = runTest {
        locationDao.insertLocation(
            LocationEntity(
                locationId = "loc-01",
                name = "Garage Fridge",
                description = null,
                locationBarcode = null,
                createdAt = 1000L,
                lastUpdated = 1000L
            )
        )
        categoryDao.insertCategory(
            CategoryEntity(
                categoryId = "food",
                name = "Food",
                description = null,
                createdAt = 1000L,
                lastUpdated = 1000L
            )
        )

        // Insert the Product
        productDao.insertProduct(
            ProductEntity(
                productId = "prod-01",
                categoryId = "food",
                name = "Milk",
                description = null,
                barcode = null,
                sku = "milk-123",
                imageUri = null,
                minimumQuantity = 1,
                createdAt = 1000L,
                lastUpdated = 1000L
            )
        )

        // Insert the Stock Batch
        stockBatchDao.insertBatch(
            StockBatchEntity(
                batchId = "batch-01",
                productId = "prod-01",
                locationId = "loc-01",
                quantity = 5,
                expirationDate = 2000L,
                price = 3.99,
                supplier = null,
                lastUpdated = 1000L
            )
        )

        // Delete the Product
        productDao.deleteProduct("prod-01")

        // Verify the cascade delete worked
        val remainingBatches = stockBatchDao.getBatchesByLocation("loc-01").first()

        assertEquals(0, remainingBatches.size)
    }

    @Test
    fun fetchProduct_returnsProductWithAllRelatedBatchesAndLocations() = runTest {
        // 1. Setup Parents
        locationDao.insertLocation(
            LocationEntity(
                locationId = "loc-01",
                name = "Garage",
                description = null,
                locationBarcode = null,
                createdAt = 1000L,
                lastUpdated = 1000L,
            )
        )
        locationDao.insertLocation(
            LocationEntity(
                locationId = "loc-02",
                name = "Kitchen",
                description = null,
                locationBarcode = null,
                createdAt = 1000L,
                lastUpdated = 1000L,
            )
        )
        categoryDao.insertCategory(
            CategoryEntity(
                categoryId = "food",
                name = "Food",
                description = null,
                createdAt = 1000L,
                lastUpdated = 1000L
            )
        )

        // 2. Insert 1 Product
        productDao.insertProduct(
            ProductEntity(
                productId = "prod-01",
                categoryId = "food",
                name = "Milk",
                description = null,
                barcode = "123456789",
                sku = null,
                imageUri = null,
                minimumQuantity = 2,
                createdAt = 1000L,
                lastUpdated = 1000L
            )
        )

        // 3. Insert 2 Batches (One in Kitchen, One in Garage)
        stockBatchDao.insertBatch(
            StockBatchEntity(
                batchId = "batch-01",
                productId = "prod-01",
                locationId = "loc-01",
                quantity = 2,
                expirationDate = 2000L,
                price = 3.99,
                lastUpdated = 1000L,
                supplier = null
            )
        )
        stockBatchDao.insertBatch(
            StockBatchEntity(
                batchId = "batch-02",
                productId = "prod-01",
                locationId = "loc-02",
                quantity = 5,
                expirationDate = 3000L,
                price = 3.99,
                lastUpdated = 1000L,
                supplier = null
            )
        )

        // 4. Fetch the relation
        // Since getProduct returns a Flow, we use .first() to grab the first emitted value
        val productWithDetails = productDao.getProduct("prod-01").first()

        // 5. Assert Room stitched it all together correctly
        assertEquals("Milk", productWithDetails.product.name)
        assertEquals("Food", productWithDetails.category.name)
        assertEquals(2, productWithDetails.batches.size) // Proves one-to-many works

        // Check that the nested locations mapped correctly
        val locations = productWithDetails.batches.map { it.location.name }
        assertTrue(locations.contains("Kitchen"))
        assertTrue(locations.contains("Garage"))
    }
}