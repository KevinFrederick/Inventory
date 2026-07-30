package com.kevinfreyap.product.presentation.screen.product_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.kevinfreyap.product.domain.model.query.sort.SortDirection
import com.kevinfreyap.product.domain.usecase.GetAllCategoryUseCase
import com.kevinfreyap.product.domain.usecase.GetAllLocationUseCase
import com.kevinfreyap.product.domain.usecase.GetFilteredProductUseCase
import com.kevinfreyap.product.presentation.action.FilterQueryAction
import com.kevinfreyap.product.presentation.mapper.toDomain
import com.kevinfreyap.product.presentation.mapper.toUi
import com.kevinfreyap.product.presentation.mapper.toUiModel
import com.kevinfreyap.product.presentation.state.FilterOptionList
import com.kevinfreyap.product.presentation.model.DateUi
import com.kevinfreyap.product.presentation.state.FilterState
import com.kevinfreyap.product.presentation.util.DateFormatter.formatDayMonthYearDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getFilteredProduct: GetFilteredProductUseCase,
    private val getAllCategory: GetAllCategoryUseCase,
    private val getAllLocation: GetAllLocationUseCase
): ViewModel() {
    private val _filtersOptionList = MutableStateFlow(FilterOptionList())
    val availableFilters = _filtersOptionList.asStateFlow()

    private val _filterState = MutableStateFlow(FilterState())

    private val _draftFilter = MutableStateFlow(FilterState())
    val draftFilter = _draftFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val productFlow = combine(
        flow = _searchQuery.debounce(300.milliseconds),
        flow2 =_filterState
    ) { currentQuery, activeFilterState ->
        activeFilterState.toDomain(currentQuery)
    }
        .flatMapLatest { currentFilter ->
            getFilteredProduct(currentFilter)
        }
        .map { pagingData ->
            pagingData.map { product ->
                product.toUiModel()
            }
        }
        .cachedIn(viewModelScope)

    init {
        loadFilterOptions()
    }

    private fun loadFilterOptions() {
        viewModelScope.launch {
            combine(
                getAllCategory(),
                getAllLocation()
            ) { categories, locations ->
                FilterOptionList(
                    categories = categories.toUi(),
                    locations = locations.toUi()
                )
            }
                .collect { newState ->
                    _filtersOptionList.value = newState
                }
        }
    }

    fun onFilterAction(action: FilterQueryAction) {
        when(action) {
            is FilterQueryAction.UpdateSearchQuery -> {
                _searchQuery.value = action.query
            }
            is FilterQueryAction.UpdateSortOption -> {
                _draftFilter.update { currentFilterState ->
                    currentFilterState.copy(
                        sortConfig = currentFilterState.sortConfig.copy(option = action.sortOption)
                    )
                }
            }
            is FilterQueryAction.ToggleSortDirection -> {
                _draftFilter.update { currentFilterState ->
                    val currentDirection = currentFilterState.sortConfig.direction
                    val newDirection = if (currentDirection == SortDirection.ASCENDING) SortDirection.DESCENDING else SortDirection.ASCENDING

                    currentFilterState.copy(
                        sortConfig = currentFilterState.sortConfig.copy(direction = newDirection)
                    )
                }
            }
            is FilterQueryAction.ToggleCategory -> {
                _draftFilter.update { currentDraft ->
                    val currentSet = currentDraft.categorySet
                    val clickedCategory = action.category

                    val newCategories = if (clickedCategory in currentSet) {
                        currentDraft.categorySet - action.category
                    } else {
                        currentDraft.categorySet + action.category
                    }
                    currentDraft.copy(categorySet = newCategories)
                }
            }
            is FilterQueryAction.UpdateLocation -> {
                _draftFilter.update { it.copy(location = action.location) }
            }
            is FilterQueryAction.UpdateDateOption -> {
                _draftFilter.update { it.copy(filterDateOption = action.dateOption) }
            }
            is FilterQueryAction.OpenDatePicker -> {
                _draftFilter.update { currentDraft ->
                    currentDraft.copy(activeDatePicker = action.activePicker)
                }
            }
            is FilterQueryAction.UpdateStartDate -> {
                val startModel = DateUi(
                    displayText = formatDayMonthYearDate(action.startDateMillis),
                    rawMillis = action.startDateMillis
                )

                _draftFilter.update { currentDraft ->
                    currentDraft.copy(
                        startDate = startModel,
                        activeDatePicker = null
                    )
                }
            }
            is FilterQueryAction.UpdateEndDate -> {
                val endModel = DateUi(
                    displayText = formatDayMonthYearDate(action.endDateMillis),
                    rawMillis = action.endDateMillis
                )

                _draftFilter.update { currentDraft ->
                    currentDraft.copy(
                        endDate = endModel,
                        activeDatePicker = null
                    )
                }
            }
            FilterQueryAction.DismissDatePicker -> {
                _draftFilter.update { it.copy(activeDatePicker = null) }
            }
            FilterQueryAction.ShowAppliedFilter -> {
                _draftFilter.value = _filterState.value
            }
            FilterQueryAction.ClearAll -> {
                _draftFilter.value = FilterState()
            }
            FilterQueryAction.ApplyFilter -> {
                _filterState.value = _draftFilter.value
            }
        }
    }
}