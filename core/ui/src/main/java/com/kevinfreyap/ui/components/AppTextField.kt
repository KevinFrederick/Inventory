package com.kevinfreyap.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    readOnly: Boolean = false,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        trailingIcon = trailingIcon,
        label = {
            Text(
                text = label,
                fontWeight = FontWeight.Medium,
            )
        },
        isError = isError,
        supportingText = errorMessage?.let {
            {
                Text(
                    text = it
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction,
            capitalization = capitalization
        ),
        keyboardActions = KeyboardActions (
            onDone = {
                focusManager.clearFocus()
            }
        ),
        singleLine = minLines == 1,
        minLines = minLines,
        readOnly = readOnly,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Theme.custom.hint,
            focusedBorderColor = MaterialTheme.colorScheme.primary,

            unfocusedLabelColor = Theme.custom.hint,
            focusedLabelColor = MaterialTheme.colorScheme.primary,

            unfocusedTrailingIconColor = Theme.custom.hint,
            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            errorTrailingIconColor = MaterialTheme.colorScheme.error
        ),
        modifier = modifier
            .fillMaxWidth()
    )
}

@Preview(
    showBackground = true
)
@Composable
fun AppTextFieldPreview() {
    var previewValue by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    LaunchedEffect(Unit) {
        focusManager.clearFocus()
    }

    InventoryTheme {
        AppTextField(
            value = previewValue,
            onValueChange = { newValue -> previewValue = newValue },
            label = "Product Name",
            isError = true,
            errorMessage = "Something wrong here",
            modifier = Modifier.padding(8.dp),
            minLines = 1,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
            trailingIcon = null
        )
    }
}