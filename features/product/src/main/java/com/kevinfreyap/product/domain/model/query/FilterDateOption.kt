package com.kevinfreyap.product.domain.model.query

import androidx.annotation.StringRes
import com.kevinfreyap.product.R

enum class FilterDateOption (
    @param:StringRes val stringRes: Int
) {
    LAST_7_DAYS (R.string.sort_option_date_last_7_days),
    THIS_MONTH (R.string.sort_option_date_this_month),
    PICK_DATE (R.string.sort_option_date_pick_date)
}