package com.kevinfreyap.product.presentation.mapper

import com.kevinfreyap.product.domain.model.CategoryId
import com.kevinfreyap.product.domain.model.LocationId
import com.kevinfreyap.product.domain.model.query.ProductQueryFilter
import com.kevinfreyap.product.presentation.state.FilterState

fun FilterState.toDomain(searchQuery: String): ProductQueryFilter {
    val domainCategories = this.categorySet.map { CategoryId(it.id) }

    val domainLocationId = this.location?.let { LocationId(it.id) }
    
    return ProductQueryFilter(
        searchQuery = searchQuery.ifBlank { null },
        sortConfig = this.sortConfig,
        categoryList = domainCategories.ifEmpty { null },
        locationId = domainLocationId,
        filterStockOption = this.filterStockOption,
        filterDateOption = this.filterDateOption,
        startDate = this.startDate?.rawMillis,
        endDate = this.endDate?.rawMillis
    )
}