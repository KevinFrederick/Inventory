package com.kevinfreyap.product.presentation.screen.add_product.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.product.R
import com.kevinfreyap.product.domain.model.error.ProductCategoryError
import com.kevinfreyap.product.domain.model.error.ProductDescriptionError
import com.kevinfreyap.product.domain.model.error.ProductFormError
import com.kevinfreyap.product.domain.model.error.ProductMinimumQuantityError
import com.kevinfreyap.product.domain.model.error.ProductNameError
import com.kevinfreyap.product.presentation.action.AddProductAction
import com.kevinfreyap.product.presentation.components.FieldListHeader
import com.kevinfreyap.product.presentation.state.ProductDetailState
import com.kevinfreyap.product.presentation.util.ThousandSeparatorVisualTransformation
import com.kevinfreyap.ui.components.AppDropdownField
import com.kevinfreyap.ui.components.AppTextField
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun SectionProductInformation(
    productDetailState: ProductDetailState,
    formErrors: ProductFormError?,
    onAction: (AddProductAction.ProductDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    FieldListHeader(
        title = stringResource(R.string.label_product_detail),
        modifier = modifier,
        fieldsColumn = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AppTextField(
                    value = productDetailState.productName,
                    onValueChange = { name ->
                        onAction(AddProductAction.ProductDetailAction.OnNameChanged(name))
                    },
                    label = stringResource(R.string.label_field_product_name),
                    minLines = 1,
                    maxLines = 1,
                    unfocusedColor = Theme.custom.hint,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences,
                    isError = formErrors?.nameError != null,
                    errorMessage = if (formErrors?.nameError != null) {
                        when(formErrors.nameError) {
                            ProductNameError.EMPTY -> stringResource(R.string.error_product_name_empty)
                            ProductNameError.TOO_LONG -> stringResource(R.string.error_product_name_too_long)
                            ProductNameError.CONTAINS_NEWLINE -> stringResource(R.string.error_product_name_contains_newline)
                        }
                    } else null
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    AppDropdownField(
                        value = productDetailState.productCategoryName,
                        label = stringResource(R.string.label_field_product_category),
                        options = productDetailState.filteredCategories,
                        optionText = { it },
                        onSearchQueryChange = { typedString ->
                            onAction(AddProductAction.ProductDetailAction.OnCategoryChanged(typedString))
                        },
                        onOptionSelected = { selectedCategory ->
                            onAction(AddProductAction.ProductDetailAction.OnCategoryChanged(selectedCategory))
                        },
                        unfocusedColor = Theme.custom.hint,
                        enableAddNew = true,
                        onAddNewOption = {
                            onAction(AddProductAction.ProductDetailAction.OnCreateNewCategory(productDetailState.productCategoryName))
                        },
                        addNewText = stringResource(R.string.dropdown_new_category, productDetailState.productCategoryName),
                        isError = formErrors?.categoryError != null,
                        errorMessage = if (formErrors?.categoryError != null) {
                            when(formErrors.categoryError) {
                                ProductCategoryError.EMPTY -> stringResource(R.string.error_product_category_empty)
                                ProductCategoryError.TOO_LONG -> stringResource(R.string.error_product_category_too_long)
                                ProductCategoryError.CONTAINS_NEWLINE -> stringResource(R.string.error_product_category_contains_newline)
                            }
                        } else null,
                        modifier = Modifier
                            .weight(0.7f)
                    )

                    AppTextField(
                        value = productDetailState.productMinQuantity,
                        onValueChange = { qty ->
                            val onlyDigit = qty.filter { it.isDigit() }

                            val cleanNumber = when {
                                onlyDigit.isEmpty() -> "0"
                                productDetailState.productMinQuantity == "0" && onlyDigit.length == 2 -> {
                                    onlyDigit.replaceFirst("0", "")
                                }
                                else -> onlyDigit.trimStart('0').ifEmpty { "0" }
                            }

                            onAction(AddProductAction.ProductDetailAction.OnMinQuantityChanged(cleanNumber))
                        },
                        label = stringResource(R.string.label_field_product_min_quantity),
                        minLines = 1,
                        maxLines = 1,
                        unfocusedColor = Theme.custom.hint,
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next,
                        visualTransformation = ThousandSeparatorVisualTransformation,
                        isError = (formErrors?.minQuantityError != null && formErrors.minQuantityError != ProductMinimumQuantityError.REQUIRES_CONFIRMATION),
                        errorMessage = if (formErrors?.minQuantityError != null){
                            when(formErrors.minQuantityError) {
                                ProductMinimumQuantityError.INVALID_FORMAT -> stringResource(R.string.error_product_min_qty_invalid_format)
                                ProductMinimumQuantityError.CANNOT_BE_NEGATIVE -> stringResource(R.string.error_product_min_qty_cannot_negative)
                                else -> null
                            }
                        } else null,
                        modifier = Modifier
                            .weight(0.3f)
                    )
                }


                AppTextField(
                    value = productDetailState.productDescription ?: "",
                    onValueChange = { text ->
                        onAction(AddProductAction.ProductDetailAction.OnDescriptionChanged(text))
                    },
                    label = stringResource(R.string.label_field_product_description),
                    minLines = 5,
                    maxLines = 5,
                    unfocusedColor = Theme.custom.hint,
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences,
                    modifier = Modifier
                        .nestedScroll(rememberNestedScrollInteropConnection()),
                    isError = formErrors?.descriptionError != null,
                    errorMessage = if (formErrors?.descriptionError != null) {
                        when (formErrors.descriptionError) {
                            ProductDescriptionError.TOO_LONG -> stringResource(R.string.error_product_description_too_long)
                        }
                    } else null,
                )
            }
        }
    )
}

@Preview(
    showBackground = true
)
@Composable
fun SectionProductInformationPreview() {
    InventoryTheme {
        SectionProductInformation(
            productDetailState = ProductDetailState(),
            formErrors = ProductFormError(),
            onAction = {}
        )
    }
}