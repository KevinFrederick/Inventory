package com.kevinfreyap.product.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.kevinfreyap.product.presentation.state.DateFieldUiState
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun DateDisplayBoxRow(
    startDateState: DateFieldUiState,
    endDateState: DateFieldUiState,
    onStartDateClicked: () -> Unit,
    onEndDateClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        DateDisplayBox(
            label = stringResource(R.string.label_start_date),
            dateString = startDateState.displayText,
            onClick = onStartDateClicked,
            isPlaceholder = startDateState.isPlaceholder,
            modifier = Modifier
                .weight(1f)
        )

        Text(
            text = stringResource(R.string.label_to),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = Theme.custom.primaryText,
            modifier = Modifier
                .padding(horizontal = 4.dp)
        )

        DateDisplayBox(
            label = stringResource(R.string.label_end_date),
            dateString = endDateState.displayText,
            onClick = onEndDateClicked,
            isPlaceholder = endDateState.isPlaceholder,
            modifier = Modifier
                .weight(1f)
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
            startDateState = DateFieldUiState(
                displayText = "20 July 2020",
                isPlaceholder = true
            ),
            endDateState = DateFieldUiState(
                displayText = "20 September 2025",
                isPlaceholder = false
            ),
            onStartDateClicked = {},
            onEndDateClicked = {}
        )
    }
}