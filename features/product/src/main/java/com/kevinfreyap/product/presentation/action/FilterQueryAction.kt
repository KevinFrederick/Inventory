package com.kevinfreyap.product.presentation.action

import com.kevinfreyap.product.domain.model.query.FilterDateOption
import com.kevinfreyap.product.domain.model.query.FilterStockOption
import com.kevinfreyap.product.domain.model.query.sort.SortOption
import com.kevinfreyap.product.presentation.model.ActiveDatePicker
import com.kevinfreyap.product.presentation.model.CategoryUi
import com.kevinfreyap.product.presentation.model.LocationUi

sealed interface FilterQueryAction {
    data class UpdateSearchQuery(val query: String): FilterQueryAction
    data class UpdateSortOption(val sortOption: SortOption): FilterQueryAction
    object ToggleSortDirection: FilterQueryAction
    data class ToggleCategory(val category: CategoryUi): FilterQueryAction
    data class UpdateLocation(val location: LocationUi): FilterQueryAction
    data class UpdateDateOption(val dateOption: FilterDateOption): FilterQueryAction
    data class UpdateStockOption(val stockOption: FilterStockOption): FilterQueryAction
    data class OpenDatePicker(val activePicker: ActiveDatePicker): FilterQueryAction
    data class UpdateStartDate(val startDateMillis: Long): FilterQueryAction
    data class UpdateEndDate(val endDateMillis: Long): FilterQueryAction
    object DismissDatePicker: FilterQueryAction
    object ShowAppliedFilter: FilterQueryAction
    object ClearAll: FilterQueryAction
    object ApplyFilter: FilterQueryAction
}