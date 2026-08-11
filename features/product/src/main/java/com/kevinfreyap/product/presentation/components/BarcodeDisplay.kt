package com.kevinfreyap.product.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kevinfreyap.product.R
import com.kevinfreyap.product.presentation.barcode.generateBarcodeBitmap
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.ui.util.shimmerEffect

@Composable
fun BarcodeDisplay(
    barcodeText: String?,
    modifier: Modifier = Modifier
) {
    val barcodeBitmap = remember(barcodeText) {
        generateBarcodeBitmap(barcodeText)
    }

    Surface(
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (barcodeBitmap != null) {
                Image(
                    bitmap = barcodeBitmap.asImageBitmap(),
                    contentDescription = "Barcode for $barcodeText",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = barcodeText!!,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            } else if (barcodeText.isNullOrBlank()) {
                Text(
                    text = stringResource(R.string.warning_no_barcode),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Theme.custom.secondaryText
                )
            } else {
                Text(
                    text = stringResource(R.string.warning_invalid_barcode),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Theme.custom.secondaryText
                )
            }
        }
    }
}

@Composable
fun BarcodeDisplayPlaceholder(
    shimmerColor: Color = Theme.custom.shimmer
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect(shimmerColor)
            )

            Text(
                text = "",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 2.sp,
                color = Color.Transparent,
                modifier = Modifier
                    .clearAndSetSemantics {}
                    .fillMaxWidth(0.4f)
                    .clip(RoundedCornerShape(50))
                    .shimmerEffect(shimmerColor)
            )
        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0x000000
)
@Composable
fun BarcodeDisplayPreview() {
    InventoryTheme {
        BarcodeDisplay(
            barcodeText = "1234567890"
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0x000000
)
@Composable
fun BarcodeDisplayPlaceholderPreview() {
    InventoryTheme {
        BarcodeDisplayPlaceholder()
    }
}