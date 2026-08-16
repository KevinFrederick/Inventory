package com.kevinfreyap.product.presentation.screen.batch_form.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.product.R
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun SectionBatchHeadline(
    productName: String,
    batchShortId: String?,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = productName,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = Theme.custom.primaryText
        )

        batchShortId?.let {
            Text(
                text = stringResource(R.string.placeholder_batch_name, it),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Normal,
                color = Theme.custom.secondaryText
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SectionBatchHeadlinePreview() {
    InventoryTheme {
        SectionBatchHeadline(
            productName = "Tablet",
            batchShortId = "123AB3"
        )
    }
}