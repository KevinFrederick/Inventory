package com.kevinfreyap.database.query

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

class ProductQueryBuilder {
    fun build(filter: ProductDbFilter): SupportSQLiteQuery {
        val query = StringBuilder(
            "SELECT p.* FROM product AS p"
        )
        val bindArgs = mutableListOf<Any>()

        val stockBatchColumnName = listOf("price", "quantity")
        val isSortingByBatch = filter.sortBy in stockBatchColumnName
        val hasLocationFilter = !filter.locationId.isNullOrBlank()

        if ( hasLocationFilter || isSortingByBatch) {
            query.append(
                " INNER JOIN stock_batch AS b ON p.productId = b.productId"
            )
        }

        var hasWhereClause = false

        fun appendCondition() {
            if (!hasWhereClause) {
                query.append(" WHERE")
                hasWhereClause = true
            } else {
                query.append(" AND")
            }
        }

        if (!filter.searchQuery.isNullOrBlank()) {
            appendCondition()
            query.append(" p.name LIKE ?")
            bindArgs.add("%${filter.searchQuery}%")
        }

        if (!filter.categoryList.isNullOrEmpty()) {
            // Create as many "?" as items in the list
            appendCondition()
            val placeholder = filter.categoryList.joinToString(separator = ",") { "?" }
            query.append(" p.categoryId IN ($placeholder)")
            bindArgs.addAll(filter.categoryList)
        }

        if (!filter.locationId.isNullOrBlank()) {
            appendCondition()
            query.append(" b.locationId = ?")
            bindArgs.add(filter.locationId)
        }

        if (filter.startDate != null && filter.endDate == null) {
            appendCondition()
            query.append(" p.createdAt >= ?")
            bindArgs.add(filter.startDate)
        }

        else if (filter.startDate == null && filter.endDate != null) {
            appendCondition()
            query.append(" p.createdAt <= ?")
            bindArgs.add(filter.endDate)
        }

        else if (filter.startDate != null) {
            appendCondition()
            query.append(" p.createdAt BETWEEN ? AND ?")
            bindArgs.add(filter.startDate)
            bindArgs.add(filter.endDate!!)
        }

        if (hasLocationFilter || isSortingByBatch) {
            query.append(" GROUP BY p.productId")
        }

        if (isSortingByBatch) {
            val orderClause = when(filter.sortBy) {
                "quantity" -> "SUM(b.quantity)"
                "price" -> if (filter.sortDirection == "ASC") "MIN(b.price)" else "MAX(b.price)"
                else -> "b.${filter.sortBy}"
            }
            query.append(" ORDER BY $orderClause ${filter.sortDirection}")
        } else {
            query.append(" ORDER BY p.${filter.sortBy} ${filter.sortDirection}")
        }

        return SimpleSQLiteQuery(query.toString(), bindArgs.toTypedArray())
    }
}