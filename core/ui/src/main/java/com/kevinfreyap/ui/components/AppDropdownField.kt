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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDropdownField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    filteredOptions: List<String>,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
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
                onValueChange(newValue)
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
            readOnly = readOnly,
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
            if (filteredOptions.isNotEmpty()) {
                filteredOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            onValueChange(selectionOption)
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
            onValueChange = { newValue -> previewValue = newValue },
            label = "Category",
            filteredOptions = filteredOptions,
            modifier = Modifier.padding(8.dp)
        )
    }
}