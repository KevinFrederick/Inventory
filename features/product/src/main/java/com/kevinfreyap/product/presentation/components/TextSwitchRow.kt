package com.kevinfreyap.product.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.ui.components.AppSwitch
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun TextSwitchRow(
    text: String,
    isChecked: Boolean,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .clickable (
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick(!isChecked)
            }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Theme.custom.primaryText
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Normal,
                    color = Theme.custom.secondaryText
                )
            }
        }

        Spacer(Modifier.width(16.dp))

        AppSwitch(
            checked = isChecked,
            onCheckedChange = onClick,
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun TextSwitchRowPreview() {
    InventoryTheme {
        TextSwitchRow(
            text = "Add initial stock",
            subtitle = "You can always add or adjust stock later.",
            isChecked = false,
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}