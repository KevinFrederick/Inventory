package com.kevinfreyap.product.presentation.screen.product_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kevinfreyap.product.R
import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.usecase.DeleteProductUseCase
import com.kevinfreyap.product.domain.usecase.GetProductByIdUseCase
import com.kevinfreyap.product.presentation.action.ProductDetailAction
import com.kevinfreyap.product.presentation.event.ProductDetailUiEvent
import com.kevinfreyap.product.presentation.mapper.toUiModel
import com.kevinfreyap.product.presentation.navigation.ProductDetailNavigation
import com.kevinfreyap.product.presentation.navigation.ProductScreen
import com.kevinfreyap.product.presentation.state.ScreenProductDetailState
import com.kevinfreyap.product.presentation.util.DateFormatter.formatDatePickerDate
import com.kevinfreyap.product.presentation.util.toFormattedCurrency
import com.kevinfreyap.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getProductById: GetProductByIdUseCase,
    private val deleteProductById: DeleteProductUseCase
): ViewModel(){
    private val route = savedStateHandle.toRoute<ProductScreen.ProductDetail>()
    private val productId = ProductId(route.productId)

    private val _uiEvent = Channel<ProductDetailUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val uiState: StateFlow<UiState<ScreenProductDetailState>> = getProductById(productId)
        .map { product ->
            if (product == null) {
                UiState.Error("Not Found")
            } else {
                UiState.Success(
                    ScreenProductDetailState(
                        imageUri = product.imageUri,
                        productName = product.name,
                        productCategory = product.category.name,
                        productSku = product.sku,
                        productTotalQty = product.totalQuantity,
                        productMinPrice = product.minBatchCost?.toFormattedCurrency(),
                        productMaxPrice = product.maxBatchCost?.toFormattedCurrency(),
                        productMinQty = product.minimumQuantity,
                        productNearestExpDate = formatDatePickerDate(product.nearestExpiringBatch),
                        productDescription = product.description,
                        productBarcode = product.barcode,
                        batchesPreviewList = product.batches.take(3).toUiModel(),
                        batchesList = product.batches.toUiModel(),
                        totalBatchCount = product.batches.size
                    )
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Idle
        )

    fun onAction(action: ProductDetailAction) {
        if (action == ProductDetailAction.OnEditButtonClick) {
            viewModelScope.launch {
                _uiEvent.send(ProductDetailUiEvent.Navigate(ProductDetailNavigation.EditProduct(productId.value)))
            }
        }
    }

    fun deleteProduct() {
        viewModelScope.launch {
            try {
                deleteProductById(productId)

                _uiEvent.send(
                    ProductDetailUiEvent.ShowToast(R.string.success_product_deleted)
                )

                _uiEvent.send(
                    ProductDetailUiEvent.Navigate(ProductDetailNavigation.NavigateUp)
                )

            } catch (_: Exception) {
                _uiEvent.send(
                    ProductDetailUiEvent.ShowToast(R.string.error_delete_product)
                )
            }
        }
    }
}