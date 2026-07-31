package com.kevinfreyap.database

import androidx.sqlite.db.SimpleSQLiteQuery
import com.kevinfreyap.database.query.ProductDbFilter
import com.kevinfreyap.database.query.ProductQueryBuilder
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ProductQueryBuilderTest {
    private val builder = ProductQueryBuilder()

    @Test
    fun `when filter is empty, generate base query with default sorting`() {
        val filter = ProductDbFilter()

        val query = builder.build(filter) as SimpleSQLiteQuery

        assertEquals(
            "SELECT * FROM product ORDER BY createdAt DESC",
            query.sql
        )
    }

    @Test
    fun `when search and location provided, generates correct AND clause`() {
        val filter = ProductDbFilter(
            searchQuery = "laptop",
            locationId = "loc-01",
            sortBy = "name",
            sortDirection = "ASC"
        )

        val query = builder.build(filter) as SimpleSQLiteQuery

        assertEquals(
            "SELECT * FROM product WHERE name LIKE ? AND locationId = ? ORDER BY name ASC",
            query.sql
        )
    }

    @Test
    fun `when date range provided, generates BETWEEN clause`() {
        // Arrange
        val filter = ProductDbFilter(
            startDate = 1000L,
            endDate = 5000L
        )

        // Act
        val query = builder.build(filter) as SimpleSQLiteQuery

        // Assert
        assertEquals(
            "SELECT * FROM product WHERE createdAt BETWEEN ? AND ? ORDER BY createdAt DESC",
            query.sql
        )
    }
}