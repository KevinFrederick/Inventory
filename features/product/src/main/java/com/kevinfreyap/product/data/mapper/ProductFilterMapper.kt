package com.kevinfreyap.product.data.mapper

import com.kevinfreyap.database.query.ProductDbFilter
import com.kevinfreyap.product.domain.model.query.FilterDateOption
import com.kevinfreyap.product.domain.model.query.ProductQueryFilter
import com.kevinfreyap.product.domain.util.DateCalculator.calculateDateRange

fun ProductQueryFilter.toDbFilter(): ProductDbFilter {
    val calculatedDate = calculateDateRange(this.filterDateOption)

    val startDate = if (this.filterDateOption == FilterDateOption.PICK_DATE) {
        this.startDate
    } else {
        calculatedDate.startMillis
    }

    val endDate = if (this.filterDateOption == FilterDateOption.PICK_DATE) {
        this.endDate
    } else {
        calculatedDate.endMillis
    }

    return ProductDbFilter (
        searchQuery = this.searchQuery,
        sortBy = this.sortConfig.option.columnName,
        sortDirection = this.sortConfig.direction.sqlString,
        categoryList = this.categoryList?.map { it.value },
        locationId = this.locationId?.value,
        stockOption = this.filterStockOption?.name,
        startDate = startDate,
        endDate = endDate
    )
}