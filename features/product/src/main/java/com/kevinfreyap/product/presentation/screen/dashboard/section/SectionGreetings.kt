package com.kevinfreyap.product.presentation.screen.dashboard.section

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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.ui.util.shimmerEffect

@Composable
fun SectionGreetings(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = Theme.custom.primaryText
        )

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = Theme.custom.secondaryText
        )
    }
}

@Composable
fun SectionGreetingsPlaceholder(
    shimmerColor: Color = Theme.custom.shimmer
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "",
            style = MaterialTheme.typography.titleLarge,
            color = Color.Transparent,
            modifier = Modifier
                .clearAndSetSemantics {
                    contentDescription = "Loading data"
                }
                .fillMaxWidth(0.7f)
                .clip(RoundedCornerShape(50))
                .shimmerEffect(shimmerColor)
        )

        Text(
            text = "",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Transparent,
            modifier = Modifier
                .clearAndSetSemantics {
                    contentDescription = "Loading data"
                }
                .fillMaxWidth(0.4f)
                .clip(RoundedCornerShape(50))
                .shimmerEffect(shimmerColor)
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SectionGreetingsPreview() {
    InventoryTheme {
        SectionGreetings(
            title = "Good Morning, User",
            subtitle = "Data last synced at 09:11"
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SectionGreetingsPlaceholderPreview() {
    InventoryTheme {
        SectionGreetingsPlaceholder()
    }
}