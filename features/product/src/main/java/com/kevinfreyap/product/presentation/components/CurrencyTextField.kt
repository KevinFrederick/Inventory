package com.kevinfreyap.product.presentation.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun CurrencyTextField(
    price: String,
    placeholder: String,
    currencySymbol: String,
    onPriceChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    unfocusedColor: Color = Theme.custom.hint,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    errorMessage: String? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val isFocused by interactionSource.collectIsFocusedAsState()

    val dividerColor = if (isFocused) {
        MaterialTheme.colorScheme.primary
    } else if (isError) {
        MaterialTheme.colorScheme.error
    } else {
        unfocusedColor
    }

    OutlinedTextField(
        value = price,
        onValueChange = onPriceChange,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyLarge,
                color = unfocusedColor
            )
        },
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = unfocusedColor,
            focusedBorderColor = MaterialTheme.colorScheme.primary,

            unfocusedLabelColor = unfocusedColor,
            focusedLabelColor = MaterialTheme.colorScheme.primary,

            unfocusedTrailingIconColor = unfocusedColor,
            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            errorTrailingIconColor = MaterialTheme.colorScheme.error
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        leadingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(56.dp)
            ) {
                Text(
                    text = currencySymbol,
                    style = MaterialTheme.typography.bodyLarge,
                    color = dividerColor,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )

                VerticalDivider(
                    color = dividerColor,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(end = 8.dp)
                )
            }
        },
        isError = isError,
        supportingText = errorMessage?.let {
            {
                Text(
                    text = it
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
    )
}

@Preview(
    showBackground = true
)
@Composable
fun CurrencyTextFieldPreview() {
    InventoryTheme {
        CurrencyTextField(
            price = "",
            placeholder = "Unit Cost (Optional)",
            currencySymbol = "Rp",
            onPriceChange = {},
        )
    }
}