package com.kevinfreyap.product.presentation.screen.product_detail.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.product.R
import com.kevinfreyap.product.presentation.components.BatchListItem
import com.kevinfreyap.product.presentation.components.BatchListItemPlaceholder
import com.kevinfreyap.product.presentation.model.StockBatchUi
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.ui.util.shimmerEffect

@Composable
fun SectionBatches(
    batches: List<StockBatchUi>,
    totalBatchCount: Int,
    onSeeAllBatchClick: () -> Unit,
    onBatchItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val maxPreviewItems = 3

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
                text = stringResource(R.string.label_product_batches),
                style = MaterialTheme.typography.labelLarge,
                color = Theme.custom.secondaryText,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )

            if (totalBatchCount > maxPreviewItems) {
                Text(
                    text = stringResource(R.string.txt_btn_label_see_all_batch, totalBatchCount),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .clickable {
                            onSeeAllBatchClick()
                        }
                        .padding(8.dp)
                )
            }
        }

        if (batches.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
            ) {
                Text(
                    text = stringResource(R.string.warning_no_active_batch),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Theme.custom.secondaryText
                )
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                batches.forEach { batchUi ->
                    val shortId = batchUi.id.substring(6, 12).uppercase()

                    BatchListItem(
                        name = stringResource(R.string.placeholder_batch_name, shortId),
                        location = batchUi.location,
                        quantity = batchUi.quantity,
                        cost = batchUi.price,
                        onBatchClick = {
                            onBatchItemClick(batchUi.id)
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun SectionBatchesPlaceholder(
    shimmerColor: Color = Theme.custom.shimmer
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "",
                style = MaterialTheme.typography.labelLarge,
                color = Color.Transparent,
                modifier = Modifier
                    .clearAndSetSemantics {}
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(0.3f)
                    .clip(RoundedCornerShape(50))
                    .shimmerEffect(shimmerColor)
            )

            Text(
                text = "",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Transparent,
                modifier = Modifier
                    .clearAndSetSemantics {}
                    .padding(8.dp)
                    .fillMaxWidth(0.3f)
                    .clip(RoundedCornerShape(50))
                    .shimmerEffect(shimmerColor)
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) {
                BatchListItemPlaceholder()
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SectionBatchesPreview() {
    InventoryTheme {
        SectionBatches(
            batches = listOf(
                StockBatchUi(
                    id = "batch-550e84",
                    quantity = 2,
                    price = "Rp 1.000.000",
                    location = "Garage",
                    expDate = "31 September 2020",
                    supplier = null
                ),
                StockBatchUi(
                    id = "batch-50e84a",
                    quantity = 3,
                    price = "",
                    location = "Garage",
                    expDate = "31 September 2020",
                    supplier = null
                ),
                StockBatchUi(
                    id = "batch-0e84b3",
                    quantity = 2,
                    price = "Rp 1.000.000",
                    location = "Garage",
                    expDate = "31 September 2020",
                    supplier = null
                ),
            ),
            totalBatchCount = 7,
            onSeeAllBatchClick = {  },
            onBatchItemClick = {  },
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SectionBatchesPlaceholderPreview() {
    InventoryTheme {
        SectionBatchesPlaceholder()
    }
}