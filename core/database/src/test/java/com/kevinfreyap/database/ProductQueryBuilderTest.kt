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
            "SELECT p.* FROM product AS p WHERE p.syncState != 'DELETED' ORDER BY p.createdAt DESC",
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
            "SELECT p.*" +
            " FROM product AS p" +
            " LEFT JOIN stock_batch AS b ON p.productId = b.productId AND b.syncState != 'DELETED'" +
            " WHERE p.syncState != 'DELETED' AND p.name LIKE ? AND b.locationId = ?" +
            " GROUP BY p.productId" +
            " ORDER BY p.name ASC",
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
            "SELECT p.* FROM product AS p" +
            " WHERE p.syncState != 'DELETED' AND p.createdAt BETWEEN ? AND ?" +
            " ORDER BY p.createdAt" +
            " DESC",
            query.sql
        )
    }

    @Test
    fun `when sorting by batch column, generates JOIN, GROUP BY, and aggregate ORDER BY`() {
        // Arrange
        val filter = ProductDbFilter(
            sortBy = "price",
            sortDirection = "ASC"
        )

        // Act
        val query = builder.build(filter) as SimpleSQLiteQuery

        // Assert
        assertEquals(
            "SELECT p.*" +
                    " FROM product AS p" +
                    " LEFT JOIN stock_batch AS b ON p.productId = b.productId AND b.syncState != 'DELETED'" +
                    " WHERE p.syncState != 'DELETED'" +
                    " GROUP BY p.productId" +
                    " ORDER BY MIN(b.price) ASC," +
                    " p.createdAt DESC",
            query.sql
        )
    }
}