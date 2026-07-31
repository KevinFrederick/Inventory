package com.kevinfreyap.database

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.kevinfreyap.database.dao.CategoryDao
import com.kevinfreyap.database.dao.LocationDao
import com.kevinfreyap.database.dao.ProductDao
import com.kevinfreyap.database.entity.CategoryEntity
import com.kevinfreyap.database.entity.LocationEntity
import com.kevinfreyap.database.entity.ProductEntity
import com.kevinfreyap.database.query.ProductDbFilter
import com.kevinfreyap.database.query.ProductQueryBuilder
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class ProductDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var productDao: ProductDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var locationDao: LocationDao
    private lateinit var queryBuilder: ProductQueryBuilder

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        locationDao = database.locationDao()
        categoryDao = database.categoryDao()
        productDao = database.productDao()
        queryBuilder = ProductQueryBuilder()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun filterProduct_byCategory_returnCorrectItems() = runTest {
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

        categoryDao.insertCategory(
            CategoryEntity(
                categoryId = "electronic",
                name = "Electronic",
                description = null,
                createdAt = 1000L,
                lastUpdated = 1000L
            )
        )
        categoryDao.insertCategory(
            CategoryEntity(
                categoryId = "furniture",
                name = "Furniture",
                description = null,
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

        productDao.insertProduct(
            ProductEntity(
                productId = "prod-01",
                categoryId = "electronic",
                name = "Laptop",
                description = null,
                barcode = "123456789",
                sku = "bgr-01",
                imageUri = null,
                minimumQuantity = 2,
                createdAt = 1000L,
                lastUpdated = 1000L
            )
        )
        productDao.insertProduct(
            ProductEntity(
                productId = "prod-02",
                categoryId = "furniture",
                name = "Desk",
                description = null,
                barcode = "123456789",
                sku = "bgr-02",
                imageUri = null,
                minimumQuantity = 2,
                createdAt = 1000L,
                lastUpdated = 1000L
            )
        )
        productDao.insertProduct(
            ProductEntity(
                productId = "prod-03",
                categoryId = "food",
                name = "Chip",
                description = null,
                barcode = "123456789",
                sku = null,
                imageUri = null,
                minimumQuantity = 2,
                createdAt = 1000L,
                lastUpdated = 1000L
            )
        )

        val filter = ProductDbFilter(
            categoryList = listOf("electronic", "food")
        )
        val query = queryBuilder.build(filter)

        val pagingSource = productDao.getAllProduct(query)
        val loadResult = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        ) as PagingSource.LoadResult.Page

        assertEquals(2, loadResult.data.size)
        assertEquals("Laptop", loadResult.data.first().product.name)
    }
}