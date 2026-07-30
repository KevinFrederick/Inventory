package com.kevinfreyap.product.presentation.state

import com.kevinfreyap.product.domain.model.query.FilterDateOption
import com.kevinfreyap.product.domain.model.query.sort.SortConfig
import com.kevinfreyap.product.presentation.model.ActiveDatePicker
import com.kevinfreyap.product.presentation.model.CategoryUi
import com.kevinfreyap.product.presentation.model.DateUi
import com.kevinfreyap.product.presentation.model.LocationUi

data class FilterState(
    val sortConfig: SortConfig = SortConfig(),
    val categorySet: Set<CategoryUi> = emptySet(),
    val location: LocationUi? = null,
    val filterDateOption: FilterDateOption? = null,
    val startDate: DateUi? = null,
    val endDate: DateUi? = null,
    val earliestDataDate: DateUi? = null,
    val latestDataDate: DateUi? = null,
    val activeDatePicker: ActiveDatePicker? = null
) {
    val startDateBoxState: DateFieldUiState
        get() = DateFieldUiState(
            displayText = startDate?.displayText ?: earliestDataDate?.displayText ?: "",
            isPlaceholder = startDate == null
        )

    val endDateBoxState: DateFieldUiState
        get() = DateFieldUiState(
            displayText = endDate?.displayText ?: latestDataDate?.displayText ?: "",
            isPlaceholder = endDate == null
        )

    val hasActiveFilter: Boolean
        get() = (categorySet.isNotEmpty()) ||
                (location != null) ||
                (filterDateOption != null)
}
