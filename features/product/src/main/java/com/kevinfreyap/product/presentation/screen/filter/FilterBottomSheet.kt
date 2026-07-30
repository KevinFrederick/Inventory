package com.kevinfreyap.product.presentation.screen.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.product.R
import com.kevinfreyap.product.domain.model.query.sort.SortConfig
import com.kevinfreyap.product.domain.model.query.sort.SortDirection
import com.kevinfreyap.product.domain.model.query.sort.SortOption
import com.kevinfreyap.product.presentation.action.FilterQueryAction
import com.kevinfreyap.product.presentation.components.AppDatePickerDialog
import com.kevinfreyap.product.presentation.components.DateDisplayBoxRow
import com.kevinfreyap.product.presentation.components.SortDirectionButton
import com.kevinfreyap.product.presentation.mapper.getDirectionLabelRes
import com.kevinfreyap.product.presentation.model.ActiveDatePicker
import com.kevinfreyap.product.presentation.model.CategoryUi
import com.kevinfreyap.product.presentation.model.LocationUi
import com.kevinfreyap.product.presentation.screen.filter.section.SectionCategory
import com.kevinfreyap.product.presentation.screen.filter.section.SectionDateAdded
import com.kevinfreyap.product.presentation.screen.filter.section.SectionFilterButtons
import com.kevinfreyap.product.presentation.screen.filter.section.SectionLocation
import com.kevinfreyap.product.presentation.screen.filter.section.SectionSort
import com.kevinfreyap.product.presentation.state.FilterOptionList
import com.kevinfreyap.product.presentation.state.FilterState
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    filterOptionList: FilterOptionList,
    filterState: FilterState,
    onAction: (FilterQueryAction) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        FilterBottomSheetContent(
            filterOptionListList = filterOptionList,
            filterState = filterState,
            onAction = onAction,
            modifier = modifier,
        )
    }
}

@Composable
fun FilterBottomSheetContent(
    filterOptionListList: FilterOptionList,
    filterState: FilterState,
    onAction: (FilterQueryAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .navigationBarsPadding()
    ) {
        Text(
            text = stringResource(R.string.title_filter_sort),
            style = MaterialTheme.typography.titleMedium,
            color = Theme.custom.primaryText,
        )

        SectionSort(
            selectedOption = filterState.sortConfig.option,
            onSelectOption = { option ->
                onAction(FilterQueryAction.UpdateSortOption(option))
            },
            sortDirectionButton = {
                SortDirectionButton(
                    sortDirection = filterState.sortConfig.direction,
                    sortLabelRes = filterState.sortConfig.getDirectionLabelRes(),
                    onClick = {
                        onAction(FilterQueryAction.ToggleSortDirection)
                    },
                )
            }
        )

        SectionCategory(
            categories = filterOptionListList.categories,
            selectedCategories = filterState.categorySet,
            onCategoryToggled = { categoryUi ->
                onAction(FilterQueryAction.ToggleCategory(category = categoryUi))
            }
        )

        SectionLocation(
            locationList = filterOptionListList.locations,
            selectedLocation = filterState.location,
            onSelectLocation = { location ->
                onAction(FilterQueryAction.UpdateLocation(location))
            },
        )

        SectionDateAdded(
            selectedDateOption = filterState.filterDateOption,
            onSelectDateOption = { dateOption ->
                onAction(FilterQueryAction.UpdateDateOption(dateOption))
            },
            customDateRow = {
                DateDisplayBoxRow(
                    startDateState = filterState.startDateBoxState,
                    endDateState = filterState.endDateBoxState,
                    onStartDateClicked = {
                        onAction(
                            FilterQueryAction.OpenDatePicker(
                                ActiveDatePicker.START
                            )
                        )
                    },
                    onEndDateClicked = {
                        onAction(
                            FilterQueryAction.OpenDatePicker(
                                ActiveDatePicker.END
                            )
                        )
                    }
                )
            },
        )

        SectionFilterButtons(
            onNegativeButtonClicked = {
                onAction(FilterQueryAction.ClearAll)
            },
            onPositiveButtonClicked = {
                onAction(FilterQueryAction.ApplyFilter)
            },
            modifier = Modifier
                .padding(top = 8.dp)
        )
    }

    filterState.activeDatePicker?.let { activeDatePicker ->
        val initialDateSelected = when (activeDatePicker) {
            ActiveDatePicker.START -> filterState.startDate?.rawMillis
            ActiveDatePicker.END -> filterState.endDate?.rawMillis
        }

        AppDatePickerDialog(
            initialDateMillis = initialDateSelected,
            onDateSelected = { selectedMillis ->
                if (activeDatePicker == ActiveDatePicker.START) {
                    onAction(FilterQueryAction.UpdateStartDate(selectedMillis))
                } else {
                    onAction(FilterQueryAction.UpdateEndDate(selectedMillis))
                }
            },
            onDismiss = { onAction(FilterQueryAction.DismissDatePicker) }
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun FilterBottomSheetPreview() {
    InventoryTheme {
        FilterBottomSheetContent(
            filterOptionListList = FilterOptionList(
                categories = listOf(
                    CategoryUi(
                        id = "Cat_01",
                        name = "Electronic"
                    ),
                    CategoryUi(
                        id = "Cat_02",
                        name = "Food"
                    ),
                    CategoryUi(
                        id = "Cat_03",
                        name = "Furniture"
                    )
                ),
                locations = listOf(
                    LocationUi(
                        id = "Loc_01",
                        name = "Garage"
                    ),
                    LocationUi(
                        id = "Loc_02",
                        name = "Warehouse"
                    )
                )
            ),
            filterState = FilterState(
                sortConfig = SortConfig(
                    option = SortOption.NAME,
                    direction = SortDirection.ASCENDING
                ),
                categorySet = setOf(
                    CategoryUi(
                        id = "Cat_02",
                        name = "Food"
                    )
                )
            ),
            onAction = {}
        )
    }
}