package com.kevinfreyap.product.presentation.screen.filter.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.kevinfreyap.product.R
import com.kevinfreyap.product.domain.model.query.sort.SortDirection
import com.kevinfreyap.product.domain.model.query.sort.SortOption
import com.kevinfreyap.product.presentation.components.RadioSelectionRow
import com.kevinfreyap.product.presentation.components.SortDirectionButton
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun SectionSort(
    selectedOption: SortOption,
    onSelectOption: (SortOption) -> Unit,
    sortDirectionButton: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.label_sort_by),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Theme.custom.primaryText,
            )

            sortDirectionButton()
        }

        SortOption.entries.forEach { option ->
            RadioSelectionRow(
                radioLabel = stringResource(option.displayName),
                onClick = { onSelectOption(option) },
                isSelected = (option == selectedOption)
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SectionSortPreview() {
    var selectedOption by remember { mutableStateOf(SortOption.DATE) }

    InventoryTheme {
        SectionSort(
            selectedOption = selectedOption,
            onSelectOption = { newOption -> selectedOption = newOption },
            sortDirectionButton = {
                SortDirectionButton(
                    sortDirection = SortDirection.DESCENDING,
                    sortLabelRes = R.string.sort_direction_desc_date,
                    onClick = {},
                )
            }
        )
    }
}