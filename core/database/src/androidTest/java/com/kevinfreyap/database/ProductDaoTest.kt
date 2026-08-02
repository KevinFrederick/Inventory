package com.kevinfreyap.database

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.kevinfreyap.database.dao.ProductDao
import com.kevinfreyap.database.query.ProductDbFilter
import com.kevinfreyap.database.query.ProductQueryBuilder
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class ProductDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var productDao: ProductDao

    private lateinit var queryBuilder: ProductQueryBuilder

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        productDao = database.productDao()
        queryBuilder = ProductQueryBuilder()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun filterProduct_byCategory_returnCorrectItems() = runTest {
        database.populateWithData()

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

    @Test
    fun countProduct_returnCorrectCount() = runTest {
        database.populateWithData()

        val result = productDao.getProductCount()

        assertEquals(3, result.first())
    }

    @Test
    fun countLowStockProduct_returnCorrectCount() = runTest {
        database.populateWithData()

        val result = productDao.getLowStockProductCount()

        assertEquals(1, result.first())
    }
}