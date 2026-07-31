package com.kevinfreyap.product.domain.model.query

import com.kevinfreyap.product.domain.model.CategoryId
import com.kevinfreyap.product.domain.model.LocationId
import com.kevinfreyap.product.domain.model.query.sort.SortConfig

data class ProductQueryFilter(
    val searchQuery: String? = null,
    val sortConfig: SortConfig = SortConfig(),
    val categoryList: List<CategoryId>? = null,
    val locationId: LocationId? = null,
    val filterDateOption: FilterDateOption? = null,
    val startDate: Long? = null,
    val endDate: Long? = null
)