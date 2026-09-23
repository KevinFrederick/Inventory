package com.kevinfreyap.product.presentation.screen.product_detail.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.domain.model.InventoryBarcode
import com.kevinfreyap.product.R
import com.kevinfreyap.product.presentation.components.BarcodeDisplay
import com.kevinfreyap.product.presentation.components.BarcodeDisplayPlaceholder
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.ui.util.shimmerEffect

@Composable
fun SectionBarcode(
    barcode: InventoryBarcode?,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.label_product_barcode),
            style = MaterialTheme.typography.labelLarge,
            color = Theme.custom.secondaryText,
        )

        BarcodeDisplay(
            barcode = barcode
        )
    }
}

@Composable
fun SectionBarcodePlaceholder(
    shimmerColor: Color = Theme.custom.shimmer
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "",
            style = MaterialTheme.typography.labelLarge,
            color = Color.Transparent,
            modifier = Modifier
                .clearAndSetSemantics {}
                .fillMaxWidth(0.3f)
                .clip(RoundedCornerShape(50))
                .shimmerEffect(shimmerColor)
        )

        BarcodeDisplayPlaceholder()
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SectionBarcodePreview() {
    InventoryTheme {
        SectionBarcode(
            barcode = InventoryBarcode("1234567890", "")
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SectionBarcodePlaceholderPreview() {
    InventoryTheme {
        SectionBarcodePlaceholder()
    }
}