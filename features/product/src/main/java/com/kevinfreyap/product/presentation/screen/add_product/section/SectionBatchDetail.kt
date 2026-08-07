package com.kevinfreyap.product.presentation.screen.add_product.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.product.R
import com.kevinfreyap.product.domain.model.error.BatchLocationError
import com.kevinfreyap.product.domain.model.error.BatchPriceError
import com.kevinfreyap.product.domain.model.error.BatchQuantityError
import com.kevinfreyap.product.domain.model.error.ProductFormError
import com.kevinfreyap.product.presentation.action.AddProductAction
import com.kevinfreyap.product.presentation.components.CurrencyTextField
import com.kevinfreyap.product.presentation.components.FieldListHeader
import com.kevinfreyap.product.presentation.components.QuantitySelector
import com.kevinfreyap.product.presentation.state.BatchDetailState
import com.kevinfreyap.product.presentation.util.ThousandSeparatorVisualTransformation
import com.kevinfreyap.ui.components.AppDropdownField
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun SectionBatchDetail(
    batchDetailState: BatchDetailState,
    formErrors: ProductFormError?,
    onAction: (AddProductAction.BatchDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    FieldListHeader(
        title = stringResource(R.string.label_batch_stock_detail),
        modifier = modifier,
        fieldsColumn = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.label_field_batch_quantity),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Theme.custom.primaryText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(0.3f)
                    )

                    Spacer(Modifier.width(16.dp))

                    QuantitySelector(
                        quantity = batchDetailState.batchQuantity,
                        onQuantityTextChange = { typedText ->
                            val onlyDigit = typedText.filter { it.isDigit() }

                            val cleanNumber = when {
                                onlyDigit.isEmpty() -> "0"
                                batchDetailState.batchQuantity == "0" && onlyDigit.length == 2 -> {
                                    onlyDigit.replaceFirst("0", "")
                                }
                                else -> onlyDigit.trimStart('0').ifEmpty { "0" }
                            }
                            onAction(AddProductAction.BatchDetailAction.OnBatchQuantityChanged(cleanNumber))
                        },
                        onIncrementClick = { 
                            onAction(AddProductAction.BatchDetailAction.OnQuantityIncremented)
                        },
                        onDecrementClick = {
                            onAction(AddProductAction.BatchDetailAction.OnQuantityDecremented)
                        },
                        visualTransformation = ThousandSeparatorVisualTransformation,
                        isError = (formErrors?.quantityError != null && formErrors.quantityError != BatchQuantityError.REQUIRES_CONFIRMATION),
                        errorMessage = if (formErrors?.quantityError != null) {
                            when(formErrors.quantityError) {
                                BatchQuantityError.EMPTY -> stringResource(R.string.error_batch_quantity_empty)
                                BatchQuantityError.INVALID_FORMAT -> stringResource(R.string.error_batch_quantity_invalid_format)
                                BatchQuantityError.CANNOT_BE_NEGATIVE -> stringResource(R.string.error_batch_quantity_cannot_be_negative)
                                BatchQuantityError.CANNOT_BE_ZERO -> stringResource(R.string.error_batch_quantity_cannot_be_zero)
                                else -> null
                            }
                        } else null,
                        modifier = Modifier
                            .weight(0.7f)
                    )
                }

                AppDropdownField(
                    value = batchDetailState.batchLocation,
                    label = stringResource(R.string.label_field_batch_location),
                    options = batchDetailState.filteredLocations,
                    optionText = { it },
                    onSearchQueryChange = { typedString ->
                        onAction(AddProductAction.BatchDetailAction.OnBatchLocationChanged(typedString))
                    },
                    onOptionSelected = { selectedLocation ->
                        onAction(AddProductAction.BatchDetailAction.OnBatchLocationChanged(selectedLocation))
                    },
                    unfocusedColor = Theme.custom.hint,
                    enableAddNew = true,
                    onAddNewOption = {
                        onAction(AddProductAction.BatchDetailAction.OnCreateNewLocation(batchDetailState.batchLocation))
                    },
                    addNewText = stringResource(R.string.dropdown_new_location, batchDetailState.batchLocation),
                    isError = formErrors?.locationError != null,
                    errorMessage = if (formErrors?.locationError != null) {
                        when(formErrors.locationError) {
                            BatchLocationError.EMPTY -> stringResource(R.string.error_batch_location_empty)
                            BatchLocationError.TOO_LONG -> stringResource(R.string.error_batch_location_too_long)
                            BatchLocationError.CONTAINS_NEWLINE -> stringResource(R.string.error_batch_location_contains_newline)
                        }
                    } else null,
                )

                CurrencyTextField(
                    price = batchDetailState.batchPrice,
                    placeholder = stringResource(R.string.label_field_batch_price),
                    currencySymbol = stringResource(R.string.currency_idr_rp),
                    onPriceChange = { newPrice ->
                        val onlyDigit = newPrice.filter { it.isDigit() }
                        onAction(AddProductAction.BatchDetailAction.OnBatchPriceChanged(onlyDigit))
                    },
                    visualTransformation = ThousandSeparatorVisualTransformation,
                    isError = (formErrors?.priceError != null && formErrors.priceError != BatchPriceError.REQUIRES_CONFIRMATION),
                    errorMessage = if (formErrors?.priceError != null) {
                        when(formErrors.priceError) {
                            BatchPriceError.INVALID_FORMAT -> stringResource(R.string.error_batch_price_invalid_format)
                            BatchPriceError.CANNOT_BE_NEGATIVE -> stringResource(R.string.error_batch_price_cannot_be_negative)
                            else -> null
                        }
                    } else null
                )
            }
        }
    )
}

@Preview(
    showBackground = true
)
@Composable
fun SectionBatchDetailPreview() {
    InventoryTheme {
        SectionBatchDetail(
            batchDetailState = BatchDetailState(),
            formErrors = ProductFormError(),
            onAction = {}
        )
    }
}