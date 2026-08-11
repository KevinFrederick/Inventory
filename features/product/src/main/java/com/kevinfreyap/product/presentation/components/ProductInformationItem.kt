package com.kevinfreyap.product.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.ui.util.shimmerEffect

@Composable
fun ProductInformationItem (
    title: String,
    value: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = Theme.custom.secondaryText
        )

        value()
    }
}

@Composable
fun ProductInformationItemPlaceholder(
    modifier: Modifier = Modifier,
    shimmerColor: Color = Theme.custom.shimmer
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Text(
            text = "",
            style = MaterialTheme.typography.labelLarge,
            color = Color.Transparent,
            modifier = Modifier
                .clearAndSetSemantics {}
                .fillMaxWidth(0.4f)
                .clip(RoundedCornerShape(50))
                .shimmerEffect(shimmerColor)
        )

        Text(
            text = "",
            style = MaterialTheme.typography.labelLarge,
            color = Color.Transparent,
            modifier = Modifier
                .clearAndSetSemantics {}
                .fillMaxWidth(0.2f)
                .clip(RoundedCornerShape(50))
                .shimmerEffect(shimmerColor)
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun ProductInformationItemPreview() {
    InventoryTheme {
        ProductInformationItem(
            title = "Low Stock Limit",
            value = {
                Text("2 item(s)")
            }
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun ProductInformationItemPlaceholderPreview() {
    InventoryTheme {
        ProductInformationItemPlaceholder()
    }
}