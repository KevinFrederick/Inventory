package com.kevinfreyap.product.presentation.screen.product_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kevinfreyap.product.domain.model.ProductId
import com.kevinfreyap.product.domain.usecase.GetProductByIdUseCase
import com.kevinfreyap.product.presentation.mapper.toUiModel
import com.kevinfreyap.product.presentation.navigation.ProductScreen
import com.kevinfreyap.product.presentation.state.ProductDetailState
import com.kevinfreyap.product.presentation.util.DateFormatter.formatDatePickerDate
import com.kevinfreyap.product.presentation.util.toFormattedCurrency
import com.kevinfreyap.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getProductById: GetProductByIdUseCase
): ViewModel(){
    private val route = savedStateHandle.toRoute<ProductScreen.ProductDetailRoute>()
    private val productId = ProductId(route.productId)

    val uiState: StateFlow<UiState<ProductDetailState>> = getProductById(productId)
        .map { product ->
            if (product == null) {
                UiState.Error("Not Found")
            } else {
                UiState.Success(
                    ProductDetailState(
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
}