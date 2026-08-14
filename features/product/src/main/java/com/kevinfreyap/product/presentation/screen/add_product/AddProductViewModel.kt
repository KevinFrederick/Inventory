package com.kevinfreyap.product.presentation.screen.add_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinfreyap.domain.Result
import com.kevinfreyap.product.domain.model.error.BatchPriceError
import com.kevinfreyap.product.domain.model.error.BatchQuantityError
import com.kevinfreyap.product.domain.model.error.ProductMinimumQuantityError
import com.kevinfreyap.product.domain.usecase.GetAllCategoryUseCase
import com.kevinfreyap.product.domain.usecase.GetAllLocationUseCase
import com.kevinfreyap.product.domain.usecase.InsertNewProductUseCase
import com.kevinfreyap.product.domain.usecase.ValidateBatchLocationUseCase
import com.kevinfreyap.product.domain.usecase.ValidateProductCategoryUseCase
import com.kevinfreyap.product.presentation.action.ProductFormAction
import com.kevinfreyap.product.presentation.action.SharedFormAction.sharedProductDetailAction
import com.kevinfreyap.product.presentation.action.SharedFormAction.sharedProductIdentificationAction
import com.kevinfreyap.product.presentation.state.ScreenAddProductState
import com.kevinfreyap.product.presentation.state.SharedProductFormState
import com.kevinfreyap.product.presentation.util.DateFormatter.formatDateLongToString
import com.kevinfreyap.product.presentation.util.DateFormatter.formatDatePickerDate
import com.kevinfreyap.product.presentation.util.DateFormatter.parseDateStringToLong
import com.kevinfreyap.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val getAllCategories: GetAllCategoryUseCase,
    private val getAllLocations: GetAllLocationUseCase,
    private val validateCategory: ValidateProductCategoryUseCase,
    private val validateLocation: ValidateBatchLocationUseCase,
    private val insertNewProduct: InsertNewProductUseCase
): ViewModel() {
    private val _formState = MutableStateFlow(ScreenAddProductState())
    val formState = _formState.asStateFlow()

    init {
        loadCategories()
        loadLocations()
    }

    fun onAction(action:  ProductFormAction) {
        when (action) {
            is ProductFormAction.ProductDetailAction -> handleProductDetailActions(action)
            is ProductFormAction.ProductIdentificationAction -> handleProductIdentification(action)
            is ProductFormAction.BatchDetailAction -> handleBatchDetail(action)
            is ProductFormAction.BatchInformationAction -> handleBatchInformation(action)
            is ProductFormAction.SummaryDialogAction -> handleSummaryDialog(action)
            is ProductFormAction.StatusDialogAction -> handleStatusDialog(action)
            is ProductFormAction.SaveProduct -> saveProduct()
            is ProductFormAction.ResetForm -> {
                _formState.update { ScreenAddProductState() }
            }
            is ProductFormAction.ImagePickerAction -> Unit
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            getAllCategories().collect { categories ->
                val categoryNames = categories.map { it.name }

                _formState.update { currentState ->
                    val productDetails = currentState.productDetail

                    currentState.copy(
                        productDetail = productDetails.copy(
                            allCategories = categoryNames,
                            filteredCategories = categoryNames.filter {
                                it.contains(productDetails.productCategoryName, ignoreCase = true)
                            }
                        )
                    )
                }
            }
        }
    }

    private fun loadLocations() {
        viewModelScope.launch {
            getAllLocations().collect{ locations ->
                val locationNames = locations.map { it.name }

                _formState.update { currentState ->
                    val batchDetails = currentState.batchDetail

                    currentState.copy(
                        batchDetail = batchDetails.copy(
                            allLocations = locationNames,
                            filteredLocations = locationNames.filter {
                                it.contains(batchDetails.batchLocation, ignoreCase = true)
                            }
                        )
                    )
                }
            }
        }
    }

    private inline fun updateSharedState(
        crossinline updater: (SharedProductFormState) -> SharedProductFormState
    ) {
        _formState.update { currentState ->
            // Pack
            val currentSharedState = SharedProductFormState(
                productDetail = currentState.productDetail,
                productIdentification = currentState.productIdentification,
                formErrors = currentState.formErrors
            )

            val updatedSharedState = updater(currentSharedState)

            // Unpack
            currentState.copy(
                productDetail = updatedSharedState.productDetail,
                productIdentification = updatedSharedState.productIdentification,
                formErrors = updatedSharedState.formErrors
            )
        }
    }

    private fun handleProductDetailActions(action: ProductFormAction.ProductDetailAction) {
        updateSharedState { currentShared ->
            sharedProductDetailAction(
                action = action,
                currentState = currentShared,
                validateCategory = validateCategory::invoke
            )
        }
    }

    private fun handleProductIdentification(action: ProductFormAction.ProductIdentificationAction) {
        updateSharedState { currentShared ->
            sharedProductIdentificationAction(
                action = action,
                currentState = currentShared,
            )
        }
    }

    private fun handleBatchDetail(action: ProductFormAction.BatchDetailAction) {
        when(action) {
            is ProductFormAction.BatchDetailAction.OnAddInitialStockToggled -> {
                _formState.update { currentState ->
                    currentState.copy(
                        batchDetail = currentState.batchDetail.copy(addInitialStock = action.isChecked)
                    )
                }
            }
            is ProductFormAction.BatchDetailAction.OnBatchQuantityChanged -> {
                _formState.update { currentState ->
                    currentState.copy(
                        batchDetail = currentState.batchDetail.copy(
                            batchQuantity = action.qty,
                            isQuantityConfirmed = false
                        ),
                        formErrors = currentState.formErrors?.copy(quantityError = null)
                    )
                }
            }
            is ProductFormAction.BatchDetailAction.OnQuantityIncremented -> {
                _formState.update { currentState ->
                    val current = currentState.batchDetail.batchQuantity.toIntOrNull() ?: 0
                    val newQty = (current + 1).toString()

                    currentState.copy(
                        batchDetail = currentState.batchDetail.copy(batchQuantity = newQty),
                        formErrors = currentState.formErrors?.copy(quantityError = null)
                    )
                }
            }
            is ProductFormAction.BatchDetailAction.OnQuantityDecremented -> {
                _formState.update { currentState ->
                    val current = currentState.batchDetail.batchQuantity.toIntOrNull() ?: 0
                    val newQty = if (current > 0) {
                        (current - 1).toString()
                    } else {
                        current.toString()
                    }

                    currentState.copy(
                        batchDetail = currentState.batchDetail.copy(batchQuantity = newQty),
                        formErrors = currentState.formErrors?.copy(quantityError = null)
                    )
                }
            }
            is ProductFormAction.BatchDetailAction.OnBatchPriceChanged -> {
                _formState.update { currentState ->
                    currentState.copy(
                        batchDetail = currentState.batchDetail.copy(batchPrice = action.price),
                        formErrors = currentState.formErrors?.copy(priceError = null)
                    )
                }
            }
            is ProductFormAction.BatchDetailAction.OnBatchLocationChanged -> {
                _formState.update { currentState ->
                    val newLocation = action.loc

                    currentState.copy(
                        batchDetail = currentState.batchDetail.copy(
                            batchLocation = newLocation,
                            filteredLocations = currentState.batchDetail.allLocations.filter {
                                it.contains(newLocation, ignoreCase = true)
                            }
                        ),
                        formErrors = currentState.formErrors?.copy(locationError = null)
                    )
                }
            }
            is ProductFormAction.BatchDetailAction.OnCreateNewLocation -> {
                when (
                    val validationResult = validateLocation(action.newLocName)
                ) {
                    is Result.Success -> {
                        _formState.update { currentState ->
                            currentState.copy(
                                batchDetail = currentState.batchDetail.copy(batchLocation = validationResult.data),
                                formErrors = currentState.formErrors?.copy(locationError = null)
                            )
                        }
                    }
                    is Result.Error -> {
                        _formState.update { currentState ->
                            currentState.copy(
                                formErrors = currentState.formErrors?.copy(
                                    locationError = validationResult.error
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun handleBatchInformation(action: ProductFormAction.BatchInformationAction) {
        when(action) {
            is ProductFormAction.BatchInformationAction.OnBatchExpirationChanged -> {
                _formState.update { currentState ->
                    currentState.copy(
                        batchInformation = currentState.batchInformation.copy(batchExpirationFieldText = action.expString)
                    )
                }
            }
            is ProductFormAction.BatchInformationAction.OnOpenExpirationDialog -> {
                _formState.update { currentState ->
                    val savedMillis = currentState.batchInformation.batchExpirationMillis

                    currentState.copy(
                        batchInformation = currentState.batchInformation.copy(batchExpirationFieldText = formatDateLongToString(savedMillis))
                    )
                }
            }
            is ProductFormAction.BatchInformationAction.OnBatchExpirationConfirm -> {
                _formState.update { currentState ->
                    val rawString = currentState.batchInformation.batchExpirationFieldText

                    val parsedMillis: Long? = if (rawString.length == 8) {
                        parseDateStringToLong(rawString)
                    } else null

                    val parsedPrettyString: String? = formatDatePickerDate(parsedMillis)

                    currentState.copy(
                        batchInformation = currentState.batchInformation.copy(
                            batchExpirationMillis = parsedMillis,
                            batchExpirationText = parsedPrettyString
                        ),
                        formErrors = currentState.formErrors?.copy(expirationError = null)
                    )
                }
            }
            is ProductFormAction.BatchInformationAction.OnBatchSupplierChanged -> {
                _formState.update { currentState ->
                    currentState.copy(
                        batchInformation = currentState.batchInformation.copy(batchSupplier = action.supplier),
                        formErrors = currentState.formErrors?.copy(supplierError = null)
                    )
                }
            }
        }
    }

    private fun handleSummaryDialog(action: ProductFormAction.SummaryDialogAction) {
        when(action) {
            ProductFormAction.SummaryDialogAction.OnConfirmAllWarnings -> {
                _formState.update { currentState ->
                    currentState.copy(
                        productDetail = currentState.productDetail.copy(
                            isMinQuantityConfirmed = true
                        ),
                        batchDetail = currentState.batchDetail.copy(
                            isQuantityConfirmed = true,
                            isPriceConfirmed = true
                        ),
                        showSummaryConfirmationDialog = false
                    )
                }

                saveProduct()
            }
            ProductFormAction.SummaryDialogAction.OnDismissWarningsDialog -> {
                _formState.update { currentState ->
                    currentState.copy(
                        showSummaryConfirmationDialog = false
                    )
                }
            }
        }
    }

    private fun handleStatusDialog(action: ProductFormAction.StatusDialogAction) {
        when(action) {
            ProductFormAction.StatusDialogAction.OnDismissError -> {
                _formState.update { currentState ->
                    currentState.copy(
                        uiState = UiState.Idle
                    )
                }
            }
        }
    }

    private fun saveProduct() {
        if (_formState.value.uiState is UiState.Loading) return

        _formState.update {
            it.copy(
                formErrors = null,
                uiState = UiState.Loading
            )
        }

        viewModelScope.launch {
            try {
                val currentState = _formState.value

                val result = insertNewProduct(
                    productName = currentState.productDetail.productName,
                    productCategoryName = currentState.productDetail.productCategoryName,
                    productDescription = currentState.productDetail.productDescription,
                    productImageUriString = currentState.productDetail.productImageUriString,
                    productMinQuantity = currentState.productDetail.productMinQuantity,
                    isMinQuantityConfirmed = currentState.productDetail.isMinQuantityConfirmed,
                    productBarcode = currentState.productIdentification.productBarcode,
                    productSku = currentState.productIdentification.productSku,
                    addInitialStock = currentState.batchDetail.addInitialStock,
                    batchQuantity = currentState.batchDetail.batchQuantity,
                    isQuantityConfirmed = currentState.batchDetail.isQuantityConfirmed,
                    batchPrice = currentState.batchDetail.batchPrice,
                    isPriceConfirmed = currentState.batchDetail.isPriceConfirmed,
                    batchLocation = currentState.batchDetail.batchLocation,
                    batchExpiration = currentState.batchInformation.batchExpirationMillis,
                    batchSupplier = currentState.batchInformation.batchSupplier
                )

                when (result) {
                    is Result.Success -> {
                        _formState.update {
                            it.copy(
                                uiState = UiState.Success(Unit)
                            )
                        }
                    }
                    is Result.Error -> {
                        val errors = result.error

                        val needsConfirmation =
                            errors.minQuantityError == ProductMinimumQuantityError.REQUIRES_CONFIRMATION ||
                            errors.quantityError == BatchQuantityError.REQUIRES_CONFIRMATION ||
                            errors.priceError == BatchPriceError.REQUIRES_CONFIRMATION

                        val sanitizedErrors = errors.copy(
                            minQuantityError = if (errors.minQuantityError == ProductMinimumQuantityError.REQUIRES_CONFIRMATION) {
                                null
                            } else errors.minQuantityError,
                            quantityError = if (errors.quantityError == BatchQuantityError.REQUIRES_CONFIRMATION) {
                                null
                            } else errors.quantityError,
                            priceError = if (errors.priceError == BatchPriceError.REQUIRES_CONFIRMATION) {
                                null
                            } else errors.priceError
                        )

                        _formState.update {
                            it.copy(
                                formErrors = sanitizedErrors,
                                uiState = UiState.Idle,
                                showSummaryConfirmationDialog = needsConfirmation
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _formState.update {
                    it.copy(
                        uiState = UiState.Error(
                            message = e.localizedMessage ?: "A critical database error occurred"
                        )
                    )
                }
            }
        }
    }
}