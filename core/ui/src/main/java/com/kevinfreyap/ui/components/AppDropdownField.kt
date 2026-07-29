package com.kevinfreyap.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AppDropdownField(
    value: String,
    label: String,
    options: List<T>,
    optionText: (T) -> String,
    onSearchQueryChange: (String) -> Unit,
    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    unfocusedColor: Color = Theme.custom.hint,
    readOnly: Boolean = false,
    floatingLabel: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    customTrailingIcon: @Composable (() -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        AppTextField(
            value = value,
            onValueChange = { newValue ->
                onSearchQueryChange(newValue)
                expanded = true
            },
            label = label,
            modifier = Modifier.menuAnchor(
                type = if (readOnly) {
                    ExposedDropdownMenuAnchorType.PrimaryNotEditable
                } else {
                    ExposedDropdownMenuAnchorType.PrimaryEditable
                },
                enabled = true
            ),
            trailingIcon = {
                if (customTrailingIcon != null) {
                    customTrailingIcon()
                } else {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            },
            unfocusedColor = unfocusedColor,
            readOnly = readOnly,
            floatingLabel = floatingLabel,
            isError = isError,
            errorMessage = errorMessage
        )

        ExposedDropdownMenu (
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .heightIn(max = 200.dp)
                .background(Color.Transparent)
        ) {
            if (options.isNotEmpty()) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(optionText(option)) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            } else {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "No results found",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    onClick = {},
                    enabled = false
                )
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun AppDropdownFieldPreview() {

    InventoryTheme {
        var previewValue by remember { mutableStateOf("") }

        val mockOptions = listOf("Electronics", "Clothing", "Food", "Furniture")

        val filteredOptions = if (previewValue.isBlank()) {
            mockOptions
        } else {
            mockOptions.filter { it.contains(previewValue, ignoreCase = true) }
        }

        AppDropdownField(
            value = previewValue,
            label = "Category",
            options = filteredOptions,
            optionText = { it },
            onSearchQueryChange = { newValue -> previewValue = newValue },
            onOptionSelected = { selectedOption -> previewValue = selectedOption },
            modifier = Modifier.padding(8.dp),
            unfocusedColor = Theme.custom.primaryText
        )
    }
}