package com.kevinfreyap.database.query

data class ProductQueryFilter(
    val searchQuery: String? = null,
    val sortBy: SortOption = SortOption.DATE,
    val sortDirection: SortDirection = SortDirection.DESCENDING,
    val categoryList: List<String>? = null,
    val locationId: String? = null,
    val filterDateOption: FilterDateOption? = null,
    val startDate: Long? = null,
    val endDate: Long? = null
)
