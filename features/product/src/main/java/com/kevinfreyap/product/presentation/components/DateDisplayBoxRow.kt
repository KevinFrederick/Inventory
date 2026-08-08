package com.kevinfreyap.product.presentation.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.kevinfreyap.product.presentation.model.DateUi
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun DateDisplayBoxRow(
    startDate: DateUi?,
    endDate: DateUi?,
    onStartDateClicked: () -> Unit,
    onEndDateClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        DateDisplayBox(
            label = if (startDate != null) stringResource(R.string.label_start_date) else "",
            dateString = startDate?.displayText ?: stringResource(R.string.label_start_date),
            onClick = onStartDateClicked,
            isPlaceholder = startDate == null,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )

        Text(
            text = stringResource(R.string.label_to),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = Theme.custom.primaryText,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )

        DateDisplayBox(
            label = if (endDate != null) stringResource(R.string.label_end_date) else "",
            dateString = endDate?.displayText ?: stringResource(R.string.label_end_date),
            onClick = onEndDateClicked,
            isPlaceholder = endDate == null,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0x000000
)
@Composable
fun DateDisplayBoxRowPreview() {
    InventoryTheme {
        DateDisplayBoxRow(
            startDate = DateUi(
                rawMillis = 1000L,
                displayText = "20 August 2020"
            ),
            endDate = null,
            onStartDateClicked = {},
            onEndDateClicked = {}
        )
    }
}