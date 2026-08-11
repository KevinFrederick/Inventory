package com.kevinfreyap.product.presentation.screen.product_detail.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.kevinfreyap.product.R
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.ui.util.shimmerEffect

@Composable
fun SectionDescription(
    description: String?,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.label_description),
            style = MaterialTheme.typography.labelLarge,
            color = Theme.custom.secondaryText
        )

        if (!description.isNullOrBlank()) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Theme.custom.primaryText
            )
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
            ) {
                Text(
                    text = stringResource(R.string.warning_no_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Theme.custom.secondaryText
                )
            }
        }
    }
}

@Composable
fun SectionDescriptionPlaceholder(
    shimmerColor: Color = Theme.custom.shimmer
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
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

        Spacer(Modifier.height(2.dp))

        Text(
            text = "",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Transparent,
            modifier = Modifier
                .clearAndSetSemantics {}
                .fillMaxWidth()
                .clip(RoundedCornerShape(50))
                .shimmerEffect(shimmerColor)
        )

        Text(
            text = "",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Transparent,
            modifier = Modifier
                .clearAndSetSemantics {}
                .fillMaxWidth()
                .clip(RoundedCornerShape(50))
                .shimmerEffect(shimmerColor)
        )

        Text(
            text = "",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Transparent,
            modifier = Modifier
                .clearAndSetSemantics {}
                .fillMaxWidth()
                .clip(RoundedCornerShape(50))
                .shimmerEffect(shimmerColor)
        )

        Text(
            text = "",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Transparent,
            modifier = Modifier
                .clearAndSetSemantics {}
                .fillMaxWidth()
                .clip(RoundedCornerShape(50))
                .shimmerEffect(shimmerColor)
        )

        Text(
            text = "",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Transparent,
            modifier = Modifier
                .clearAndSetSemantics {}
                .fillMaxWidth(0.6f)
                .clip(RoundedCornerShape(50))
                .shimmerEffect(shimmerColor)
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SectionDescriptionPreview() {
    InventoryTheme {
        SectionDescription(
            description = LoremIpsum(words = 50).values.first()
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SectionDescriptionPlaceholderPreview() {
    InventoryTheme {
        SectionDescriptionPlaceholder()
    }
}