package com.kevinfreyap.database.query

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

class ProductQueryBuilder {
    fun build(
        filter: ProductDbFilter,
        isCountQuery: Boolean = false
    ): SupportSQLiteQuery {
        val query = StringBuilder()

        val stockBatchColumnName = listOf("price", "quantity")
        val isSortingByBatch = filter.sortBy in stockBatchColumnName
        val hasLocationFilter = !filter.locationId.isNullOrBlank()
        val hasStockFilter = !filter.stockOption.isNullOrBlank()

        if (isCountQuery) {
            if (hasStockFilter) {
                query.append("SELECT COUNT(*) FROM (SELECT p.productId FROM product AS p")
            } else {
                query.append("SELECT COUNT (DISTINCT p.productId) FROM product AS p")
            }
        } else {
            query.append("SELECT p.* FROM product AS p")
        }

        val bindArgs = mutableListOf<Any>()

        if ( hasLocationFilter || isSortingByBatch || hasStockFilter) {
            query.append(
                " LEFT JOIN stock_batch AS b ON p.productId = b.productId"
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

        val needsGroupBy = hasLocationFilter || isSortingByBatch || hasStockFilter

        if (!isCountQuery || hasStockFilter) {
            if (needsGroupBy) {
                query.append(" GROUP BY p.productId")
            }

            if (hasStockFilter) {
                when(filter.stockOption) {
                    "OUT_OF_STOCK" -> {
                        query.append(" HAVING COALESCE(SUM(b.quantity), 0) <= 0")
                    }
                    "LOW_STOCK" -> {
                        query.append(" HAVING COALESCE(SUM(b.quantity), 0) > 0 AND COALESCE(SUM(b.quantity), 0) <= p.minimumQuantity")
                    }
                    "STOCK_WARNING" -> {
                        query.append(" HAVING COALESCE(SUM(b.quantity), 0) <= p.minimumQuantity")
                    }
                    "IN_STOCK" -> {
                        query.append(" HAVING COALESCE(SUM(b.quantity), 0) > 0")
                    }
                }
            }
        }

        if (isCountQuery && hasStockFilter) {
            query.append(")")
        }

        if (!isCountQuery) {
            if (isSortingByBatch) {
                val orderClause = when(filter.sortBy) {
                    "quantity" -> "SUM(b.quantity)"
                    "price" -> if (filter.sortDirection == "ASC") "MIN(b.price)" else "MAX(b.price)"
                    else -> "b.${filter.sortBy}"
                }
                query.append(" ORDER BY $orderClause ${filter.sortDirection}, p.createdAt DESC")
            } else {
                query.append(" ORDER BY p.${filter.sortBy} ${filter.sortDirection}")
            }
        }

        return SimpleSQLiteQuery(query.toString(), bindArgs.toTypedArray())
    }
}