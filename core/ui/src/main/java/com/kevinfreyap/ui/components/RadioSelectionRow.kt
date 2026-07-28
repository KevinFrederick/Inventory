package com.kevinfreyap.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.ui.R
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun RadioSelectionRow(
    radioLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(
                top = 8.dp,
                bottom = 8.dp,
                end = 8.dp
            )
    ) {
        Text(
            text = radioLabel,
            style = MaterialTheme.typography.bodyMedium,
            color = Theme.custom.primaryText,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
        )

        if (isSelected) {
            Icon(
                painter = painterResource(R.drawable.outline_radio_button_checked_24),
                contentDescription = "Radio Selected",
                tint = MaterialTheme.colorScheme.primary
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.radio_button_unchecked_24),
                contentDescription = "Radio UnSelected",
                tint = Theme.custom.primaryText
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun RadioSelectionRowPreviewSelected() {
    InventoryTheme {
        RadioSelectionRow(
            radioLabel = "Date",
            isSelected = true,
            onClick = {}
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun RadioSelectionRowPreviewNotSelected() {
    InventoryTheme {
        RadioSelectionRow(
            radioLabel = "Date",
            isSelected = false,
            onClick = {}
        )
    }
}