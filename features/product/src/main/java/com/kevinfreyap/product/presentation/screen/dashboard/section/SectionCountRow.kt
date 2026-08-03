package com.kevinfreyap.product.presentation.screen.dashboard.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.product.R
import com.kevinfreyap.product.presentation.components.ProductCountDisplayBox
import com.kevinfreyap.product.presentation.components.ProductCountDisplayBoxPlaceholder
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun SectionCountRow(
    totalProductValue: Int,
    lowStockValue: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        ProductCountDisplayBox(
            label = stringResource(R.string.label_total_product),
            value = totalProductValue.toString(),
            modifier = Modifier
                .weight(1f)
        )

        ProductCountDisplayBox(
            label = stringResource(R.string.label_low_stock_product),
            value = lowStockValue.toString(),
            valueColor = if (lowStockValue > 0) {
                Theme.custom.warning
            } else {
                Theme.custom.primaryText
            },
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Composable
fun SectionCountRowPlaceholder() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ProductCountDisplayBoxPlaceholder(
            modifier = Modifier
                .weight(1f)
        )

        ProductCountDisplayBoxPlaceholder(
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SectionCountRowPreview() {
    InventoryTheme {
        SectionCountRow(
            totalProductValue = 1700,
            lowStockValue = 7,
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SectionCountRowPlaceholderPreview() {
    InventoryTheme {
        SectionCountRowPlaceholder()
    }
}