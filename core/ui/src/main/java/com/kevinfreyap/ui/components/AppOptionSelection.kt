package com.kevinfreyap.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun <T> AppOptionSelection(
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    titleText: (T) -> String,
    modifier: Modifier = Modifier,
    groupTitle: String? = null,
    descriptionText: (T) -> String? = { null },
) {
    Column(modifier = modifier.selectableGroup()) {
        if (groupTitle != null) {
            Text(
                text = groupTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
            )
        }

        options.forEachIndexed { index, option ->
            val isSelected = option == selectedOption

            SelectableCardItem(
                isSelected = isSelected,
                onClick = { onOptionSelected(option) },
                title = titleText(option),
                description = descriptionText(option)
            )

            if (index < options.lastIndex) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun SelectableCardItem(
    isSelected: Boolean,
    onClick: () -> Unit,
    title: String,
    description: String?,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        Theme.custom.hint
    }

    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primary.copy(0.1f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = onClick,
                role = Role.RadioButton
            ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 0.5.dp,
            color = borderColor
        ),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = null
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Theme.custom.primaryText
                )

                if (description != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Theme.custom.secondaryText
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun AppOptionSelectionPreview() {
    InventoryTheme {
        AppOptionSelection(
            options = listOf("Option 1", "Option 2", "Option 3"),
            selectedOption = "Option 2",
            onOptionSelected = {},
            titleText = { it },
            descriptionText = { null },
            groupTitle = "Group Title"
        )
    }
}