package com.kevinfreyap.product.presentation.screen.bottom_sheet.filter.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.product.R
import com.kevinfreyap.product.domain.model.query.FilterStockOption
import com.kevinfreyap.product.presentation.components.OptionChip
import com.kevinfreyap.product.presentation.mapper.toLabel
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun SectionStock(
    selectedOption: FilterStockOption?,
    onSelectOption: (FilterStockOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.label_stock_status),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = Theme.custom.primaryText,
        )

        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(FilterStockOption.entries) {filterStockOption ->
                OptionChip(
                    label = stringResource(filterStockOption.toLabel()),
                    onClick = { onSelectOption(filterStockOption) },
                    isSelected = (filterStockOption == selectedOption)
                )
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SectionStockPreview() {
    InventoryTheme {
        SectionStock(
            selectedOption = FilterStockOption.IN_STOCK,
            onSelectOption = {},
        )
    }
}