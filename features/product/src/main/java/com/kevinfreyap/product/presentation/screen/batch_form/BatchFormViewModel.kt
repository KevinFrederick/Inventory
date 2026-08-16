package com.kevinfreyap.product.presentation.screen.batch_form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kevinfreyap.domain.Result
import com.kevinfreyap.domain.util.toShortId
import com.kevinfreyap.product.R
import com.kevinfreyap.product.domain.model.BatchId
import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.model.error.BatchPriceError
import com.kevinfreyap.product.domain.model.error.BatchQuantityError
import com.kevinfreyap.product.domain.model.error.DatabaseError
import com.kevinfreyap.product.domain.usecase.DeleteBatchUseCase
import com.kevinfreyap.product.domain.usecase.GetAllLocationUseCase
import com.kevinfreyap.product.domain.usecase.GetBatchByIdUseCase
import com.kevinfreyap.product.domain.usecase.GetProductByIdUseCase
import com.kevinfreyap.product.domain.usecase.SaveBatchUseCase
import com.kevinfreyap.product.domain.usecase.ValidateBatchLocationUseCase
import com.kevinfreyap.product.presentation.action.ProductFormAction
import com.kevinfreyap.product.presentation.action.SharedFormAction.sharedBatchDetailAction
import com.kevinfreyap.product.presentation.action.SharedFormAction.sharedBatchInformationAction
import com.kevinfreyap.product.presentation.navigation.BatchFormNavigation
import com.kevinfreyap.ui.event.UiEvent
import com.kevinfreyap.product.presentation.navigation.ProductScreen
import com.kevinfreyap.product.presentation.state.ScreenBatchFormState
import com.kevinfreyap.product.presentation.state.SharedBatchFormState
import com.kevinfreyap.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BatchFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val saveBatch: SaveBatchUseCase,
    private val deleteBatchById: DeleteBatchUseCase,
    private val getBatchById: GetBatchByIdUseCase,
    private val getProductById: GetProductByIdUseCase,
    private val getAllLocations: GetAllLocationUseCase,
    private val validateLocation: ValidateBatchLocationUseCase
): ViewModel() {
    private val route = savedStateHandle.toRoute<ProductScreen.BatchForm>()

    private val productId = ProductId(route.productId)
    private val batchId = route.batchId?.let { BatchId(it) }

    private val _formState = MutableStateFlow(ScreenBatchFormState())
    val formState = _formState.asStateFlow()

    private val _uiEvent = Channel<UiEvent<BatchFormNavigation>>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadInitialData()
        loadLocations()
    }

    fun onAction(action: ProductFormAction) {
        when(action) {
            is ProductFormAction.BatchDetailAction -> handleBatchDetail(action)
            is ProductFormAction.BatchInformationAction -> handleBatchInformation(action)
            is ProductFormAction.SummaryDialogAction -> handleSummaryDialog(action)
            is ProductFormAction.StatusDialogAction -> handleStatusDialog(action)
            is ProductFormAction.BatchToggleAction -> handleBatchToggleDialog(action)
            is ProductFormAction.Save -> addOrUpdateBatch()
            is ProductFormAction.ImagePickerAction -> Unit
            is ProductFormAction.ProductDetailAction -> Unit
            is ProductFormAction.ProductIdentificationAction -> Unit
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            if (batchId == null) {
                val product = getProductById(productId).firstOrNull()

                if (product == null) {
                    _formState.update { currentState ->
                        currentState.copy(
                            uiState = UiState.Error("Product not found")
                        )
                    }
                    return@launch
                }

                _formState.update {
                    it.copy(
                        productName = product.name,
                        batchShortId = null,
                        isExistingBatch = false,
                        isReadOnly = false,
                        uiState = UiState.Idle
                    )
                }

                return@launch
            }

            val batchDetails = getBatchById(batchId).firstOrNull()

            if (batchDetails == null) {
                _formState.update { currentState ->
                    currentState.copy(
                        uiState = UiState.Error("Batch not found")
                    )
                }

                return@launch
            }

            _formState.update { currentState ->
                val initialDetailState = currentState.batchDetail.copy(
                    batchQuantity = batchDetails.batch.quantity.toString(),
                    batchPrice = batchDetails.batch.price.toLong().toString(),
                    batchLocation = batchDetails.batch.location.name,
                )

                val initialIdentificationState = currentState.batchInformation.copy(
                    batchExpirationMillis = batchDetails.batch.expirationDate,
                    batchSupplier = batchDetails.batch.supplier
                )

                currentState.copy(
                    productName = batchDetails.productName,
                    batchShortId = batchDetails.batch.batchId.value.toShortId(),

                    originalBatchDetail = initialDetailState,
                    originalBatchInformation = initialIdentificationState,

                    batchDetail = initialDetailState,
                    batchInformation = initialIdentificationState,

                    uiState = UiState.Idle,

                    isExistingBatch = true,
                    isReadOnly = true
                )
            }
        }
    }

    private fun loadLocations() {
        viewModelScope.launch {
            getAllLocations().collect{ locations ->
                val locationNames = locations.map { it.name }

                _formState.update { currentState ->
                    val batchDetails = currentState.batchDetail
                    val originalDetails = currentState.originalBatchDetail

                    val filteredList = locationNames.filter {
                        it.contains(batchDetails.batchLocation, ignoreCase = true)
                    }

                    currentState.copy(
                        batchDetail = batchDetails.copy(
                            allLocations = locationNames,
                            filteredLocations = filteredList
                        ),

                        originalBatchDetail = originalDetails.copy(
                            allLocations = locationNames,
                            filteredLocations = filteredList
                        )
                    )
                }
            }
        }
    }

    private inline fun updateSharedBatchState(
        crossinline updater: (SharedBatchFormState) -> SharedBatchFormState
    ) {
        _formState.update { currentState ->
            // Pack
            val currentSharedState = SharedBatchFormState(
                batchDetail = currentState.batchDetail,
                batchInformation = currentState.batchInformation,
                formErrors = currentState.formErrors
            )

            val updatedSharedState = updater(currentSharedState)

            // Unpack
            currentState.copy(
                batchDetail = updatedSharedState.batchDetail,
                batchInformation = updatedSharedState.batchInformation,
                formErrors = updatedSharedState.formErrors
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
        when (action) {
            ProductFormAction.SummaryDialogAction.OnConfirmAllWarnings -> {
                updateSharedBatchState { currentShared ->
                    currentShared.copy(
                        batchDetail = currentShared.batchDetail.copy(
                            isQuantityConfirmed = true,
                            isPriceConfirmed = true
                        )
                    )
                }

                _formState.update { currentState ->
                    currentState.copy(showSummaryConfirmationDialog = false)
                }

                addOrUpdateBatch()
            }

            ProductFormAction.SummaryDialogAction.OnDismissWarningsDialog -> {
                _formState.update { currentState ->
                    currentState.copy(showSummaryConfirmationDialog = false)
                }
            }
        }
    }

    private fun handleStatusDialog(action: ProductFormAction.StatusDialogAction) {
        when(action) {
            ProductFormAction.StatusDialogAction.OnDismissError -> {
                _formState.update { currentState ->
                    currentState.copy(uiState = UiState.Idle)
                }
            }
        }
    }

    private fun handleBatchToggleDialog(action: ProductFormAction.BatchToggleAction) {
        when(action) {
            ProductFormAction.BatchToggleAction.ToggleEditMode -> {
                _formState.update { currentState ->
                    currentState.copy(
                        isReadOnly = false
                    )
                }
            }
        }
    }

    private fun addOrUpdateBatch() {
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

                val result = saveBatch.invoke(
                    productId = productId,
                    batchId = batchId,
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

                        _uiEvent.send(UiEvent.ShowToast(R.string.success_batch_saved))
                        _uiEvent.send(UiEvent.Navigate(BatchFormNavigation.NavigateUp))

                        _formState.update { ScreenBatchFormState() }
                    }
                    is Result.Error -> {
                        val errors = result.error

                        if (errors.databaseError == DatabaseError.NOT_FOUND) {
                            _formState.update {
                                it.copy(
                                    uiState = UiState.Error("Cannot update batch: Batch has been deleted or not found."),
                                    formErrors = null
                                )
                            }
                            return@launch
                        }

                        val needsConfirmation =
                                errors.quantityError == BatchQuantityError.REQUIRES_CONFIRMATION ||
                                errors.priceError == BatchPriceError.REQUIRES_CONFIRMATION

                        val sanitizedErrors = errors.copy(
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

    fun deleteBatch() {
        if (batchId == null) return

        viewModelScope.launch {
            _formState.update { it.copy(uiState = UiState.Loading) }

            try {
                deleteBatchById(
                    batchId = batchId,
                    productId = productId
                )

                _uiEvent.send(
                    UiEvent.ShowToast(R.string.success_batch_deleted)
                )

                _uiEvent.send(
                    UiEvent.Navigate(BatchFormNavigation.NavigateUp)
                )

            } catch (_: Exception) {
                _formState.update { it.copy(uiState = UiState.Idle) }

                _uiEvent.send(
                    UiEvent.ShowToast(R.string.error_delete_batch)
                )
            }
        }
    }
}