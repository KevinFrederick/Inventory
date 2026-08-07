package com.kevinfreyap.product.presentation.screen.dashboard.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.product.presentation.model.ProductListItemUi
import com.kevinfreyap.product.presentation.model.StockLevel
import com.kevinfreyap.ui.components.AppBaseListItem
import com.kevinfreyap.ui.components.AppBaseListItemPlaceholder
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.ui.util.shimmerEffect

@Composable
fun <T> SectionListWithHeader(
    title: String,
    list: List<T>,
    modifier: Modifier = Modifier,
    textButton: @Composable (() -> Unit)? = null,
    itemContent: @Composable (T) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = Theme.custom.primaryText
        )

        list.forEach { item ->
            itemContent(item)
        }

        if (list.size > 3) {
            textButton?.invoke()
        }
    }
}

@Composable
fun SectionListWithHeaderPlaceholder(
    shimmerColor: Color = Theme.custom.shimmer,
    isTextButtonExist: Boolean = true
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "",
            style = MaterialTheme.typography.titleMedium,
            color = Theme.custom.primaryText,
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .clip(RoundedCornerShape(50))
                .shimmerEffect(shimmerColor)
        )

        repeat(3) {
            AppBaseListItemPlaceholder(shimmerColor)
        }

        if (isTextButtonExist) {
            Text(
                text = "",
                style = MaterialTheme.typography.labelLarge,
                color = Color.Transparent,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clearAndSetSemantics {
                        contentDescription = "Loading data"
                    }
                    .fillMaxWidth(0.4f)
                    .padding(
                        vertical = 8.dp,
                    )
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(50))
                    .shimmerEffect(shimmerColor)
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFF8F9FA
)
@Composable
fun SectionListWithHeaderPreview() {
    InventoryTheme {
        SectionListWithHeader(
            title = "Needs Attention",
            textButton = {
                Text(
                    text = "View all 7 products",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(50))
                        .clickable  {

                        }
                        .padding(
                            vertical = 8.dp,
                        )
                )
            },
            list = listOf(
                ProductListItemUi(
                    id = "PROD-01",
                    name = "Smartphone",
                    quantity = 100,
                    stockLevel = StockLevel.IN_STOCK,
                    category = "Electronic",
                    imageUri = null,
                    sku = "#SKU-1234-B"
                ),
                ProductListItemUi(
                    id = "PROD-02",
                    name = "Smartphone 2",
                    quantity = 10,
                    stockLevel = StockLevel.LOW_STOCK,
                    category = "Electronic",
                    imageUri = null,
                    sku = "#SKU-1234-B"
                ),
                ProductListItemUi(
                    id = "PROD-03",
                    name = "Smartphone 3",
                    quantity = 0,
                    stockLevel = StockLevel.OUT_OF_STOCK,
                    category = "Electronic",
                    imageUri = null,
                    sku = "#SKU-1234-B"
                ),
            )
        ) { product ->
            AppBaseListItem(
                itemName = product.name,
                onClick = {},
                subtitle = {},
                trailingData = {},
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SectionListWithHeaderPlaceholderPreview() {
    InventoryTheme {
        SectionListWithHeaderPlaceholder()
    }
}