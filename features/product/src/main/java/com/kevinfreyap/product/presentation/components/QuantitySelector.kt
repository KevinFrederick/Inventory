package com.kevinfreyap.product.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.ui.R as coreR
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun QuantitySelector(
    quantity: String,
    onQuantityTextChange: (String) -> Unit,
    onIncrementClick: () -> Unit,
    onDecrementClick: () -> Unit,
    modifier: Modifier = Modifier,
    unfocusedColor: Color = Theme.custom.hint,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    errorMessage: String? = null,
    isReadOnly: Boolean = false
) {
    val interactionSource = remember { MutableInteractionSource() }

    val isFocused by interactionSource.collectIsFocusedAsState()

    val selectorColor = if (isFocused) {
        MaterialTheme.colorScheme.primary
    } else if (isError) {
        MaterialTheme.colorScheme.error
    } else {
        unfocusedColor
    }

     OutlinedTextField(
        value = quantity,
        onValueChange = { newValue ->
            val filteredText = newValue.filter { it.isDigit() }
            onQuantityTextChange(filteredText)
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = unfocusedColor,
            focusedBorderColor = MaterialTheme.colorScheme.primary,

            unfocusedLabelColor = unfocusedColor,
            focusedLabelColor = MaterialTheme.colorScheme.primary,

            disabledLabelColor = unfocusedColor,
            disabledBorderColor = unfocusedColor,
            disabledTrailingIconColor = unfocusedColor,
            disabledTextColor = Theme.custom.primaryText,

            unfocusedTrailingIconColor = unfocusedColor,
            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            errorTrailingIconColor = MaterialTheme.colorScheme.error
        ),
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        readOnly = isReadOnly,
        enabled = !isReadOnly,
        leadingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(48.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(48.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                        .clickable(
                            onClick = onDecrementClick,
                            enabled = !isReadOnly
                        ),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        painter = painterResource(coreR.drawable.remove_24),
                        contentDescription = "Decrease Quantity",
                        tint = selectorColor
                    )
                }

                VerticalDivider(
                    color = selectorColor,
                    modifier = Modifier.fillMaxHeight()
                )
            }
        },
        trailingIcon = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(48.dp)
            ) {
                VerticalDivider(
                    color = selectorColor,
                    modifier = Modifier.fillMaxHeight()
                )

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(48.dp)
                        .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                        .clickable(
                            onClick = onIncrementClick,
                            enabled = !isReadOnly
                        ),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        painter = painterResource(coreR.drawable.add_24),
                        contentDescription = "Decrease Quantity",
                        tint = selectorColor
                    )
                }

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
    )
}

@Preview(
    showBackground = true
)
@Composable
fun QuantitySelectorPreview() {
    InventoryTheme {
        QuantitySelector(
            quantity = "100",
            onQuantityTextChange = {  },
            onIncrementClick = {  },
            onDecrementClick = {  },
        )
    }
}