package com.kevinfreyap.product.presentation.screen.add_product.section

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.product.R
import com.kevinfreyap.product.domain.model.error.BatchExpirationError
import com.kevinfreyap.product.domain.model.error.BatchSupplierError
import com.kevinfreyap.product.domain.model.error.ProductFormError
import com.kevinfreyap.product.presentation.action.AddProductAction
import com.kevinfreyap.product.presentation.components.FieldListHeader
import com.kevinfreyap.product.presentation.state.BatchInformationState
import com.kevinfreyap.ui.util.DateInputFieldValidationHelper.isValidPartialDate
import com.kevinfreyap.product.presentation.util.DateInputTransformation
import com.kevinfreyap.ui.components.AppDateInputDialog
import com.kevinfreyap.ui.components.AppTextField
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun SectionBatchInformation(
    batchInformationState: BatchInformationState,
    formErrors: ProductFormError?,
    onAction: (AddProductAction.BatchInformationAction) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }

    FieldListHeader(
        title = stringResource(R.string.label_batch_stock_information),
        modifier = modifier,
        fieldsColumn = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box {
                    AppTextField(
                        value = batchInformationState.batchExpirationText,
                        onValueChange = {},
                        label = stringResource(R.string.label_field_batch_expiration),
                        minLines = 1,
                        maxLines = 1,
                        unfocusedColor = Theme.custom.hint,
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.calendar_month_24),
                                contentDescription = "Expiration Date"
                            )
                        },
                        isError = formErrors?.expirationError != null,
                        errorMessage = if (formErrors?.expirationError != null){
                            when(formErrors.expirationError) {
                                BatchExpirationError.CANNOT_BE_IN_PAST -> stringResource(R.string.error_batch_expiration_cannot_be_past)
                            }
                        } else null,
                    )

                    Spacer(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable {
                                onAction(AddProductAction.BatchInformationAction.OnOpenExpirationDialog)
                                showDatePicker = true
                            }
                    )
                }

                AppTextField(
                    value = batchInformationState.batchSupplier ?: "",
                    onValueChange = { supplier ->
                        onAction(AddProductAction.BatchInformationAction.OnBatchSupplierChanged(supplier))
                    },
                    label = stringResource(R.string.label_field_batch_supplier),
                    minLines = 1,
                    maxLines = 1,
                    unfocusedColor = Theme.custom.hint,
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Sentences,
                    isError = formErrors?.supplierError != null,
                    errorMessage = if (formErrors?.supplierError != null) {
                        when(formErrors.supplierError) {
                            BatchSupplierError.TOO_LONG -> stringResource(R.string.error_batch_supplier_too_long)
                        }
                    } else null,
                )
            }
        }
    )

    if (showDatePicker) {
        AppDateInputDialog(
            dateValue = batchInformationState.batchExpirationFieldText,
            fieldLabel = stringResource(R.string.label_field_batch_expiration),
            fieldPlaceholder = stringResource(R.string.placeholder_date_input),
            visualTransformation = DateInputTransformation,
            onDateChanged = { newDateTyped ->
                val onlyDigit = newDateTyped.filter { it.isDigit() }
                onAction(AddProductAction.BatchInformationAction.OnBatchExpirationChanged(onlyDigit))
            },
            onConfirm = {
                onAction(AddProductAction.BatchInformationAction.OnBatchExpirationConfirm)
                showDatePicker = false
            },
            onDismiss = {
                showDatePicker = false
            },
            isError = formErrors?.expirationError != null,
            errorMessage = if (formErrors?.expirationError != null){
                when(formErrors.expirationError) {
                    BatchExpirationError.CANNOT_BE_IN_PAST -> stringResource(R.string.error_batch_expiration_cannot_be_past)
                }
            } else null,
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SectionBatchInformationPreview() {
    InventoryTheme {
        SectionBatchInformation(
            batchInformationState = BatchInformationState(),
            formErrors = ProductFormError(),
            onAction = {}
        )
    }
}