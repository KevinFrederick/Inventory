package com.kevinfreyap.product.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.ui.util.shimmerEffect

@Composable
fun ProductCountDisplayBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = Theme.custom.primaryText,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .widthIn(min = 160.dp)
            .heightIn(min = 100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = Theme.custom.secondaryText,
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = valueColor,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 16.dp, horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun ProductCountDisplayBoxPlaceholder(
    modifier: Modifier = Modifier,
    shimmerColor: Color = Theme.custom.shimmer,
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .widthIn(min = 160.dp)
            .heightIn(min = 100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "Placeholder text",
                style = MaterialTheme.typography.labelMedium,
                color = Color.Transparent,
                modifier = Modifier
                    .clearAndSetSemantics {
                        contentDescription = "Loading data"
                    }
                    .clip(RoundedCornerShape(50))
                    .shimmerEffect(shimmerColor)
            )
            Text(
                text = "100000",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = Color.Transparent,
                modifier = Modifier
                    .clearAndSetSemantics {
                        contentDescription = "Loading data"
                    }
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 16.dp, horizontal = 8.dp)
                    .clip(RoundedCornerShape(50))
                    .shimmerEffect(shimmerColor)
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun ProductCountDisplayBoxPreview() {
    InventoryTheme {
        ProductCountDisplayBox(
            label = "Total Product",
            value = "1700"
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun ProductCountDisplayBoxPlaceholderPreview() {
    InventoryTheme {
        ProductCountDisplayBoxPlaceholder()
    }
}