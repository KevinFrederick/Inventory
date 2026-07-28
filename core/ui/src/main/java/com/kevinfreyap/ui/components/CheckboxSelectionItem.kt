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
fun CheckboxSelectionItem(
    checkboxLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = 8.dp)
    ) {
        if (isSelected) {
            Icon(
                painter = painterResource(R.drawable.check_box_24),
                contentDescription = "Checkbox Checked",
                tint = MaterialTheme.colorScheme.primary
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.check_box_outline_blank_24),
                contentDescription = "Checkbox Unchecked",
                tint = Theme.custom.primaryText
            )
        }

        Text(
            text = checkboxLabel,
            style = MaterialTheme.typography.bodyMedium,
            color = Theme.custom.primaryText,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun CheckboxSelectionItemPreviewChecked() {
    InventoryTheme {
        CheckboxSelectionItem(
            checkboxLabel = "Electronic",
            isSelected = true,
            onClick = {}
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun CheckboxSelectionItemPreviewNotChecked() {
    InventoryTheme {
        CheckboxSelectionItem(
            checkboxLabel = "Electronic",
            isSelected = false,
            onClick = {}
        )
    }
}