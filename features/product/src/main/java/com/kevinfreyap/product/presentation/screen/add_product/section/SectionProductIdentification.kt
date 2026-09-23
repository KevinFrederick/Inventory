package com.kevinfreyap.product.presentation.screen.add_product.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.domain.model.InventoryBarcode
import com.kevinfreyap.product.R
import com.kevinfreyap.product.domain.model.error.ProductBarcodeError
import com.kevinfreyap.product.domain.model.error.ProductFormError
import com.kevinfreyap.product.domain.model.error.ProductSkuError
import com.kevinfreyap.product.presentation.action.ProductFormAction
import com.kevinfreyap.product.presentation.components.FieldListHeader
import com.kevinfreyap.product.presentation.state.ProductFormIdentificationState
import com.kevinfreyap.ui.components.AppTextField
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun SectionProductIdentification(
    productFormIdentificationState: ProductFormIdentificationState,
    formErrors: ProductFormError?,
    onAction: (ProductFormAction.ProductIdentificationAction) -> Unit,
    onBarcodeIconClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    FieldListHeader(
        title = stringResource(R.string.label_product_identification),
        modifier = modifier,
        fieldsColumn = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AppTextField(
                    value = productFormIdentificationState.productSku ?: "",
                    onValueChange = { sku ->
                        val sanitizedSku = sku.replace(" ", "")
                        onAction(ProductFormAction.ProductIdentificationAction.OnSkuChanged(sanitizedSku))
                    },
                    label = stringResource(R.string.label_field_product_sku),
                    minLines = 1,
                    maxLines = 1,
                    unfocusedColor = Theme.custom.hint,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Characters,
                    isError = formErrors?.skuError != null,
                    errorMessage = if (formErrors?.skuError != null) {
                        when(formErrors.skuError) {
                            ProductSkuError.TOO_LONG -> stringResource(R.string.error_product_sku_too_long)
                            ProductSkuError.CONTAINS_WHITESPACE -> stringResource(R.string.error_product_sku_contains_spaces)
                            ProductSkuError.INVALID_CHARACTERS -> stringResource(R.string.error_product_sku_invalid_characters)
                            ProductSkuError.ALREADY_EXISTS -> stringResource(R.string.error_product_sku_already_exists)
                        }
                    } else null,
                )

                AppTextField(
                    value = productFormIdentificationState.productBarcode ?: "",
                    onValueChange = { barcode ->
                        val sanitizedBarcode = barcode.replace(" ", "")
                        onAction(
                            ProductFormAction.ProductIdentificationAction.OnBarcodeChanged(
                                InventoryBarcode(
                                    value = sanitizedBarcode,
                                    format = "CODE_128"
                                )
                            )
                        )
                    },
                    label = stringResource(R.string.label_field_product_barcode),
                    minLines = 1,
                    maxLines = 1,
                    unfocusedColor = Theme.custom.hint,
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Ascii,
                    isError = formErrors?.barcodeError != null,
                    errorMessage = if (formErrors?.barcodeError != null) {
                        when(formErrors.barcodeError) {
                            ProductBarcodeError.TOO_LONG -> stringResource(R.string.error_product_barcode_too_long)
                            ProductBarcodeError.INVALID_CHARACTER -> stringResource(R.string.error_product_barcode_invalid_characters)
                            ProductBarcodeError.CONTAINS_WHITESPACE -> stringResource(R.string.error_product_barcode_contains_spaces)
                        }
                    } else null,
                    trailingIcon = {
                        IconButton(
                            onClick = onBarcodeIconClicked,
                            modifier = Modifier
                                .padding(end = 8.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.custom_barcode_scanner),
                                contentDescription = "Barcode Scanner"
                            )
                        }
                    }
                )
            }
        }
    )
}

@Preview(
    showBackground = true
)
@Composable
fun SectionProductIdentificationPreview() {
    InventoryTheme {
        SectionProductIdentification(
            productFormIdentificationState = ProductFormIdentificationState(),
            formErrors = ProductFormError(),
            onAction = {},
            onBarcodeIconClicked = {}
        )
    }
}