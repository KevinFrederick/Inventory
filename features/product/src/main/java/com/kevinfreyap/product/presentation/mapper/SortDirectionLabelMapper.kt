package com.kevinfreyap.product.presentation.mapper

import com.kevinfreyap.product.R
import com.kevinfreyap.product.domain.model.query.sort.SortConfig
import com.kevinfreyap.product.domain.model.query.sort.SortDirection
import com.kevinfreyap.product.domain.model.query.sort.SortOption

fun SortConfig.getDirectionLabelRes(): Int {
    return when(this.option) {
        SortOption.DATE -> {
            if (this.direction == SortDirection.ASCENDING) {
                R.string.sort_direction_asc_date
            } else {
                R.string.sort_direction_desc_date
            }
        }
        SortOption.NAME -> {
            if (this.direction == SortDirection.ASCENDING) {
                R.string.sort_direction_asc_product_name
            } else {
                R.string.sort_direction_desc_product_name
            }
        }
        SortOption.QUANTITY -> {
            if (this.direction == SortDirection.ASCENDING) {
                R.string.sort_direction_asc_quantity_price
            } else {
                R.string.sort_direction_desc_quantity_price
            }
        }
        SortOption.PRICE -> {
            if (this.direction == SortDirection.ASCENDING) {
                R.string.sort_direction_asc_quantity_price
            } else {
                R.string.sort_direction_desc_quantity_price
            }
        }
    }
}