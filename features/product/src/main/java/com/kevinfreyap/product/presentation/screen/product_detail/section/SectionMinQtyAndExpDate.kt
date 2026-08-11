package com.kevinfreyap.product.presentation.screen.product_detail.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.product.R
import com.kevinfreyap.product.presentation.components.ProductInformationItem
import com.kevinfreyap.product.presentation.components.ProductInformationItemPlaceholder
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun SectionMinQtyAndExpDate(
    minQuantity: String,
    expirationText: String?,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        ProductInformationItem(
            title = stringResource(R.string.label_low_stock_limit),
            modifier = Modifier
                .weight(1f),
            value = {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Theme.custom.primaryText,
                                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                fontWeight = FontWeight.Medium
                            )
                        ) {
                            append("$minQuantity ")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = Theme.custom.hint,
                                fontSize = MaterialTheme.typography.labelMedium.fontSize,
                                fontWeight = MaterialTheme.typography.labelMedium.fontWeight
                            )
                        ) {
                            append(stringResource(R.string.label_quantity_items))
                        }
                    }
                )
            }
        )

        ProductInformationItem(
            title = stringResource(R.string.label_nearest_expiration),
            modifier = Modifier
                .weight(1f),
            value = {
                Text(
                    text = expirationText ?: stringResource(R.string.warning_no_expiration),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Theme.custom.primaryText
                )
            }
        )
    }
}

@Composable
fun SectionMinQtyAndExpDatePlaceholder() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ProductInformationItemPlaceholder(
            modifier = Modifier.weight(1f)
        )
        ProductInformationItemPlaceholder(
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SectionMinQtyAndExpDatePreview() {
    InventoryTheme {
        SectionMinQtyAndExpDate(
            minQuantity = "2",
            expirationText = "20 September 2020"
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SectionMinQtyAndExpDatePlaceholderPreview() {
    InventoryTheme {
        SectionMinQtyAndExpDatePlaceholder()
    }
}