package com.kevinfreyap.product.domain.model.query.sort

enum class SortDirection (val sqlString: String) {
    ASCENDING ("ASC"),
    DESCENDING ("DESC")
}