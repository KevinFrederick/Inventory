package com.kevinfreyap.product.presentation.screen.filter.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.product.R
import com.kevinfreyap.product.domain.model.query.FilterDateOption
import com.kevinfreyap.product.presentation.components.DateDisplayBoxRow
import com.kevinfreyap.product.presentation.components.DateOptionChip
import com.kevinfreyap.product.presentation.state.DateFieldUiState
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun SectionDateAdded(
    selectedDateOption: FilterDateOption?,
    onSelectDateOption: (FilterDateOption) -> Unit,
    customDateRow: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.label_date_added),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = Theme.custom.primaryText,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterDateOption.entries.forEach { filterDateOption ->
                DateOptionChip(
                    label = stringResource(filterDateOption.stringRes),
                    onClick = { onSelectDateOption(filterDateOption) },
                    isSelected = (filterDateOption == selectedDateOption)
                )
            }
        }

        if (selectedDateOption == FilterDateOption.PICK_DATE) {
            Spacer(Modifier.height(8.dp))
            customDateRow()
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SectionDateAddedPreview() {
    var selectedDateOption by remember { mutableStateOf(FilterDateOption.PICK_DATE) }

    InventoryTheme {
        SectionDateAdded(
            selectedDateOption = selectedDateOption,
            onSelectDateOption = { option -> selectedDateOption = option },
            customDateRow = {
                DateDisplayBoxRow(
                    startDateState = DateFieldUiState(
                        displayText = "20 July 2020",
                        isPlaceholder = true
                    ),
                    endDateState = DateFieldUiState(
                        displayText = "20 September 2025",
                        isPlaceholder = false
                    ),
                    onStartDateClicked = {},
                    onEndDateClicked = {}
                )
            }
        )
    }
}