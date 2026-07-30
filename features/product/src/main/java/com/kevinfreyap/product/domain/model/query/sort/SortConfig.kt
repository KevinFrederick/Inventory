package com.kevinfreyap.product.domain.model.query.sort

data class SortConfig(
    val option: SortOption = SortOption.DATE,
    val direction: SortDirection = SortDirection.DESCENDING
)
