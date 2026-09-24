package com.kevinfreyap.product.presentation.screen.add_product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.model.InventoryBarcode
import com.kevinfreyap.product.R
import com.kevinfreyap.product.domain.model.error.BatchPriceError
import com.kevinfreyap.product.domain.model.error.BatchQuantityError
import com.kevinfreyap.product.domain.model.error.ProductMinimumQuantityError
import com.kevinfreyap.product.domain.usecase.GetAllCategoryUseCase
import com.kevinfreyap.product.domain.usecase.GetAllLocationUseCase
import com.kevinfreyap.product.domain.usecase.InsertNewProductUseCase
import com.kevinfreyap.product.domain.usecase.ValidateBatchLocationUseCase
import com.kevinfreyap.product.domain.usecase.ValidateProductCategoryUseCase
import com.kevinfreyap.product.presentation.action.ProductFormAction
import com.kevinfreyap.product.presentation.action.SharedFormAction.sharedBatchDetailAction
import com.kevinfreyap.product.presentation.action.SharedFormAction.sharedBatchInformationAction
import com.kevinfreyap.product.presentation.action.SharedFormAction.sharedProductDetailAction
import com.kevinfreyap.product.presentation.action.SharedFormAction.sharedProductIdentificationAction
import com.kevinfreyap.product.presentation.navigation.AddProductNavigation
import com.kevinfreyap.product.presentation.navigation.ProductScreen
import com.kevinfreyap.product.presentation.state.ScreenAddProductState
import com.kevinfreyap.product.presentation.state.SharedBatchFormState
import com.kevinfreyap.product.presentation.state.SharedProductFormState
import com.kevinfreyap.ui.event.UiEvent
import com.kevinfreyap.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAllCategories: GetAllCategoryUseCase,
    private val getAllLocations: GetAllLocationUseCase,
    private val validateCategory: ValidateProductCategoryUseCase,
    private val validateLocation: ValidateBatchLocationUseCase,
    private val insertNewProduct: InsertNewProductUseCase
): ViewModel() {
    private val route = savedStateHandle.toRoute<ProductScreen.AddProduct>()
    private val initialBarcode = if (!route.barcodeValue.isNullOrBlank() && route.barcodeFormat != null) {
        InventoryBarcode(
            value = route.barcodeValue,
            format = route.barcodeFormat
        )

    } else null

    private val _uiEvent = Channel<UiEvent<AddProductNavigation>>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _formState = MutableStateFlow(ScreenAddProductState())
    val formState = _formState.asStateFlow()

    init {
        loadCategories()
        loadLocations()

        if (initialBarcode != null) {
            handleProductIdentification(ProductFormAction.ProductIdentificationAction.OnBarcodeChanged(initialBarcode))
        }
    }

    fun onAction(action: ProductFormAction) {
        when (action) {
            is ProductFormAction.ProductDetailAction -> handleProductDetailActions(action)
            is ProductFormAction.ProductIdentificationAction -> handleProductIdentification(action)
            is ProductFormAction.BatchDetailAction -> handleBatchDetail(action)
            is ProductFormAction.BatchInformationAction -> handleBatchInformation(action)
            is ProductFormAction.SummaryDialogAction -> handleSummaryDialog(action)
            is ProductFormAction.StatusDialogAction -> handleStatusDialog(action)
            is ProductFormAction.Save -> saveProduct()
            is ProductFormAction.ImagePickerAction -> Unit
            is ProductFormAction.BatchToggleAction -> Unit
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

    private inline fun updateSharedProductState(
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

    private inline fun updateSharedBatchState(
        crossinline updater: (SharedBatchFormState) -> SharedBatchFormState
    ) {
        _formState.update { currentState ->
            val currentSharedState = SharedBatchFormState(
                batchDetail = currentState.batchDetail,
                batchInformation = currentState.batchInformation,
                formErrors = currentState.formErrors
            )

            val updatedSharedState = updater(currentSharedState)

            currentState.copy(
                batchDetail = updatedSharedState.batchDetail,
                batchInformation = updatedSharedState.batchInformation,
                formErrors = updatedSharedState.formErrors
            )
        }
    }

    private fun handleProductDetailActions(action: ProductFormAction.ProductDetailAction) {
        updateSharedProductState { currentShared ->
            sharedProductDetailAction(
                action = action,
                currentState = currentShared,
                validateCategory = validateCategory::invoke
            )
        }
    }

    private fun handleProductIdentification(action: ProductFormAction.ProductIdentificationAction) {
        updateSharedProductState { currentShared ->
            sharedProductIdentificationAction(
                action = action,
                currentState = currentShared,
            )
        }
    }

    private fun handleBatchDetail(action: ProductFormAction.BatchDetailAction) {
        updateSharedBatchState { currentShared ->
            sharedBatchDetailAction(
                action = action,
                currentState = currentShared,
                validateLocation = validateLocation::invoke
            )
        }
    }

    private fun handleBatchInformation(action: ProductFormAction.BatchInformationAction) {
        updateSharedBatchState { currentShared ->
            sharedBatchInformationAction(
                action = action,
                currentState = currentShared
            )
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
                    productBarcodeFormat = currentState.productIdentification.productBarcodeFormat,
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

                        _uiEvent.send(UiEvent.ShowToast(R.string.success_product_saved))
                        _uiEvent.send(UiEvent.Navigate(AddProductNavigation.NavigateUp))

                        _formState.update { ScreenAddProductState() }
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