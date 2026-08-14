package com.kevinfreyap.product.presentation.action

import com.kevinfreyap.domain.Result
import com.kevinfreyap.product.domain.model.error.ProductCategoryError
import com.kevinfreyap.product.presentation.state.SharedProductFormState

object SharedFormAction {
    fun sharedProductDetailAction(
        action: ProductFormAction.ProductDetailAction,
        currentState: SharedProductFormState,
        validateCategory: (String) -> Result<String, ProductCategoryError>
    ): SharedProductFormState {
        return when (action) {
            is ProductFormAction.ProductDetailAction.OnNameChanged -> {
                currentState.copy(
                    productDetail = currentState.productDetail.copy(productName = action.name),
                    formErrors = currentState.formErrors?.copy(nameError = null)
                )
            }
            is ProductFormAction.ProductDetailAction.OnCategoryChanged -> {
                val newName = action.category

                currentState.copy(
                    productDetail = currentState.productDetail.copy(
                        productCategoryName = newName,
                        filteredCategories = currentState.productDetail.allCategories.filter {
                            it.contains(newName, ignoreCase = true)
                        }
                    ),
                    formErrors = currentState.formErrors?.copy(categoryError = null)
                )
            }
            is ProductFormAction.ProductDetailAction.OnCreateNewCategory -> {
                when (
                    val validationResult = validateCategory(action.newCategory)
                ) {
                    is Result.Success -> {
                        currentState.copy(
                            productDetail = currentState.productDetail.copy(productCategoryName = validationResult.data),
                            formErrors = currentState.formErrors?.copy(categoryError = null)
                        )
                    }
                    is Result.Error -> {
                        currentState.copy(
                            formErrors = currentState.formErrors?.copy(
                                categoryError = validationResult.error
                            )
                        )
                    }
                }
            }
            is ProductFormAction.ProductDetailAction.OnDescriptionChanged -> {
                currentState.copy(
                    productDetail = currentState.productDetail.copy(productDescription = action.desc),
                    formErrors = currentState.formErrors?.copy(descriptionError = null)
                )
            }
            is ProductFormAction.ProductDetailAction.OnImageUriChanged -> {
                currentState.copy(
                    productDetail = currentState.productDetail.copy(productImageUriString = action.uri),
                    formErrors = currentState.formErrors?.copy(imageError = null)
                )
            }
            is ProductFormAction.ProductDetailAction.OnMinQuantityChanged -> {
                currentState.copy(
                    productDetail = currentState.productDetail.copy(
                        productMinQuantity = action.qty,
                        isMinQuantityConfirmed = false
                    ),
                    formErrors = currentState.formErrors?.copy(minQuantityError = null)
                )
            }
        }
    }

    fun sharedProductIdentificationAction(
        action: ProductFormAction.ProductIdentificationAction,
        currentState: SharedProductFormState
    ): SharedProductFormState {
        return when(action) {
            is ProductFormAction.ProductIdentificationAction.OnBarcodeChanged -> {
                currentState.copy(
                    productIdentification = currentState.productIdentification.copy(productBarcode = action.barcode),
                    formErrors = currentState.formErrors?.copy(barcodeError = null)
                )
            }
            is ProductFormAction.ProductIdentificationAction.OnSkuChanged -> {
                currentState.copy(
                    productIdentification = currentState.productIdentification.copy(productSku = action.sku),
                    formErrors = currentState.formErrors?.copy(skuError = null)
                )
            }
        }
    }
}