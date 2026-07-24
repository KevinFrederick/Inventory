package com.kevinfreyap.database.query

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

class ProductQueryBuilder {
    fun build(filter: ProductDbFilter): SupportSQLiteQuery {
        val query = StringBuilder(
            "SELECT * FROM product"
        )
        val bindArgs = mutableListOf<Any>()
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
            query.append(" name LIKE ?")
            bindArgs.add("%${filter.searchQuery}%")
        }

        if (!filter.categoryList.isNullOrEmpty()) {
            // Create as many "?" as items in the list
            appendCondition()
            val placeholder = filter.categoryList.joinToString(separator = ",") { "?" }
            query.append(" categoryId IN ($placeholder)")
            bindArgs.addAll(filter.categoryList)
        }

        if (!filter.locationId.isNullOrBlank()) {
            appendCondition()
            query.append(" locationId = ?")
            bindArgs.add(filter.locationId)
        }

        if (filter.startDate != null && filter.endDate == null) {
            appendCondition()
            query.append(" createdAt >= ?")
            bindArgs.add(filter.startDate)
        }

        else if (filter.startDate == null && filter.endDate != null) {
            appendCondition()
            query.append(" createdAt <= ?")
            bindArgs.add(filter.endDate)
        }

        else if (filter.startDate != null) {
            appendCondition()
            query.append(" createdAt BETWEEN ? AND ?")
            bindArgs.add(filter.startDate)
            bindArgs.add(filter.endDate!!)
        }

        query.append(" ORDER BY ${filter.sortBy} ${filter.sortDirection}")

        return SimpleSQLiteQuery(query.toString(), bindArgs.toTypedArray())
    }
}