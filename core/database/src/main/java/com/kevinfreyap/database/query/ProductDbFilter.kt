package com.kevinfreyap.database.query

data class ProductDbFilter(
    val searchQuery: String? = null,
    val sortBy: String = "createdAt",
    val sortDirection: String = "DESC",
    val categoryList: List<String>? = null,
    val locationId: String? = null,
    val stockOption: String? = null,
    val startDate: Long? = null,
    val endDate: Long? = null
)
