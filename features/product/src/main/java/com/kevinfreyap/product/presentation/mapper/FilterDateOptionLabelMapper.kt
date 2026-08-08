package com.kevinfreyap.product.presentation.mapper

import com.kevinfreyap.product.R
import com.kevinfreyap.product.domain.model.query.FilterDateOption

fun FilterDateOption.toLabel(): Int {
    return when(this) {
        FilterDateOption.LAST_7_DAYS -> R.string.sort_option_date_last_7_days
        FilterDateOption.THIS_MONTH -> R.string.sort_option_date_this_month
        FilterDateOption.PICK_DATE -> R.string.sort_option_date_pick_date
    }
}