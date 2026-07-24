package com.kevinfreyap.database.query

import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import java.time.LocalDate
import java.time.ZoneId

class ProductQueryBuilder {
    fun build(filter: ProductQueryFilter): SupportSQLiteQuery {
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

        when(filter.filterDateOption) {
            FilterDateOption.LAST_7_DAYS -> {
                val zoneId = ZoneId.systemDefault()
                val today = LocalDate.now(zoneId)

                val sevenDaysAgoDate = today.minusDays(7)

                val startOfDay = sevenDaysAgoDate.atStartOfDay(zoneId)
                val sevenDaysAgoMillis = startOfDay.toInstant().toEpochMilli()

                appendCondition()
                query.append(" createdAt >= ?")
                bindArgs.add(sevenDaysAgoMillis)
            }
            FilterDateOption.THIS_MONTH -> {
                val zoneId = ZoneId.systemDefault()
                val today = LocalDate.now(zoneId)

                val startMillis = today.withDayOfMonth(1)
                    .atStartOfDay(zoneId)
                    .toInstant()
                    .toEpochMilli()

                val endMillis = today.plusDays(1)
                    .atStartOfDay(zoneId)
                    .toInstant()
                    .toEpochMilli() - 1L

                appendCondition()
                query.append(" createdAt >= ? AND createdAt <= ?")
                bindArgs.addAll(listOf(startMillis, endMillis))
            }
            FilterDateOption.PICK_DATE -> {
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
            }
            null -> {}
        }

        val sortBy = when (filter.sortBy) {
            SortOption.DATE -> "createdAt"
            SortOption.NAME -> "name"
            SortOption.QUANTITY -> "quantity"
            SortOption.PRICE -> "price"
        }

        val direction = when (filter.sortDirection) {
            SortDirection.ASCENDING -> "ASC"
            SortDirection.DESCENDING -> "DESC"
        }

        query.append(" ORDER BY $sortBy $direction")

        return SimpleSQLiteQuery(query.toString(), bindArgs.toTypedArray())
    }
}