package com.kevinfreyap.product.presentation.screen.product_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.kevinfreyap.product.domain.model.query.sort.SortDirection
import com.kevinfreyap.product.domain.usecase.GetAllCategoryUseCase
import com.kevinfreyap.product.domain.usecase.GetAllLocationUseCase
import com.kevinfreyap.product.domain.usecase.GetFilteredProductCountUseCase
import com.kevinfreyap.product.domain.usecase.GetFilteredProductUseCase
import com.kevinfreyap.product.presentation.action.FilterQueryAction
import com.kevinfreyap.product.presentation.mapper.toDomain
import com.kevinfreyap.product.presentation.mapper.toUi
import com.kevinfreyap.product.presentation.mapper.toUiModel
import com.kevinfreyap.product.presentation.state.FilterOptionList
import com.kevinfreyap.product.presentation.model.DateUi
import com.kevinfreyap.product.presentation.model.ProductListItemUi
import com.kevinfreyap.product.presentation.state.FilterState
import com.kevinfreyap.product.presentation.util.DateFormatter.formatDatePickerDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class ProductListViewModel @Inject constructor(
    getFilteredProduct: GetFilteredProductUseCase,
    private val getFilteredProductCount: GetFilteredProductCountUseCase,
    private val getAllCategory: GetAllCategoryUseCase,
    private val getAllLocation: GetAllLocationUseCase
): ViewModel() {
    private val _filtersOptionList = MutableStateFlow(FilterOptionList())
    val availableFilters = _filtersOptionList.asStateFlow()

    private val _filterState = MutableStateFlow(FilterState())
    val filterState = _filterState.asStateFlow()

    private val _draftFilter = MutableStateFlow(FilterState())
    val draftFilter = _draftFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _appliedQuery = MutableStateFlow("")
    val appliedQuery = _appliedQuery.asStateFlow()

    private var searchJob: Job? = null

    private val pagingStream = getFilteredProduct (
        filterProvider = { _filterState.value.toDomain(_appliedQuery.value) }
    )

    val products: Flow<PagingData<ProductListItemUi>> = pagingStream.flow
        .map { pagingData ->
            pagingData.map { it.toUiModel() }
        }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val totalCount: StateFlow<Int> = combine(
        flow = _appliedQuery,
        flow2 = _filterState
    ) { query, filter ->
        filter.toDomain(query)
    }
        .flatMapLatest { filter ->
            getFilteredProductCount(filter)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

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

                searchJob?.cancel()

                searchJob = viewModelScope.launch {
                    delay(300.milliseconds)

                    _appliedQuery.value = action.query
                    pagingStream.invalidate()
                }
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
                _draftFilter.update { currentState ->
                    val newDateOption = if (currentState.filterDateOption == action.dateOption) {
                        null
                    } else {
                        action.dateOption
                    }

                    currentState.copy(filterDateOption = newDateOption)
                }
            }
            is FilterQueryAction.OpenDatePicker -> {
                _draftFilter.update { currentDraft ->
                    currentDraft.copy(activeDatePicker = action.activePicker)
                }
            }
            is FilterQueryAction.UpdateStartDate -> {
                val startModel = DateUi(
                    displayText = formatDatePickerDate(action.startDateMillis),
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
                    displayText = formatDatePickerDate(action.endDateMillis),
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
                _filterState.value = FilterState()
                pagingStream.invalidate()
            }
            FilterQueryAction.ApplyFilter -> {
                _filterState.value = _draftFilter.value
                pagingStream.invalidate()
            }
        }
    }
}