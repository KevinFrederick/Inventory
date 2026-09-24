package com.kevinfreyap.product.presentation.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinfreyap.domain.model.InventoryBarcode
import com.kevinfreyap.product.R
import com.kevinfreyap.product.domain.usecase.GetLowStockProductUseCase
import com.kevinfreyap.product.domain.usecase.GetRecentProductListUseCase
import com.kevinfreyap.product.domain.usecase.GetInventorySummaryUseCase
import com.kevinfreyap.product.domain.usecase.GetProductByBarcodeUseCase
import com.kevinfreyap.product.presentation.mapper.toUiModel
import com.kevinfreyap.product.presentation.model.AlertListUi
import com.kevinfreyap.product.presentation.navigation.DashboardNavigation
import com.kevinfreyap.product.presentation.state.ScreenDashboardState
import com.kevinfreyap.product.presentation.util.toFormattedCurrency
import com.kevinfreyap.product.presentation.util.toFormattedNumber
import com.kevinfreyap.ui.event.UiEvent
import com.kevinfreyap.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    getLowStockProduct: GetLowStockProductUseCase,
    getRecentProductList: GetRecentProductListUseCase,
    private val getProductByBarcodeUseCase: GetProductByBarcodeUseCase,
    private val getInventorySummary: GetInventorySummaryUseCase,
): ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val lowStockFlow: Flow<AlertListUi?> = getLowStockProduct()
        .map { lowStock ->
            if (lowStock.isNotEmpty()) {
                val uiProducts = lowStock.take(3).toUiModel()

                AlertListUi(
                    title = R.string.label_stock_warning,
                    textButton = R.string.btn_label_view_all_low_stock_product,
                    products = uiProducts,
                    textButtonArg = lowStock.size,
                )
            } else {
                null
            }
        }

    private val recentProductFlow: Flow<AlertListUi> = getRecentProductList()
        .map { recentList ->
            val uiProducts = recentList.toUiModel()

            AlertListUi(
                title = R.string.label_recently_updated,
                textButton = R.string.btn_label_view_all_product,
                products = uiProducts,
                textButtonArg = 0,
            )
        }

    private val retryTrigger = MutableStateFlow(0)

    private val _uiEvent = Channel< UiEvent<DashboardNavigation>>()
    val uiEvent = _uiEvent.receiveAsFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<UiState<ScreenDashboardState>> = retryTrigger
        .flatMapLatest { _ ->
            combine(
                flow = getInventorySummary(),
                flow2 = lowStockFlow,
                flow3 = recentProductFlow
            ) { inventorySummary, lowStock, recentProduct ->
                if (inventorySummary.totalProduct == 0) return@combine UiState.Empty

                UiState.Success(
                    ScreenDashboardState(
                        totalProductRaw = inventorySummary.totalProduct,
                        totalProductCount = inventorySummary.totalProduct.toFormattedNumber(),
                        totalItemsCount = inventorySummary.totalItem.toFormattedNumber(),
                        estimatedValue = inventorySummary.totalValue.toFormattedCurrency(),
                        lowStockAlert = lowStock,
                        recentProduct = recentProduct
                    )
                )
            }
                .onStart { emit(UiState.Loading) }
                .catch { exception ->
                    emit(UiState.Error(exception.message ?: "An unexpected error occurred"))
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    fun onRetryClicked() {
        retryTrigger.value += 1
    }

    fun onBarcodeScanned(barcode: InventoryBarcode) {
        viewModelScope.launch {
            val existingProduct = getProductByBarcodeUseCase(barcode)

            if (existingProduct != null) {
                _uiEvent.send(UiEvent.ShowToast(R.string.success_product_found))
                _uiEvent.send(UiEvent.Navigate(DashboardNavigation.ProductDetail(existingProduct.productId.value)))
            } else {
                _uiEvent.send(UiEvent.ShowToast(R.string.warning_product_not_found))
                _uiEvent.send(
                    UiEvent.Navigate(
                        DashboardNavigation.AddProduct(
                            barcodeValue = barcode.value,
                            barcodeFormat = barcode.format
                        )
                    )
                )
            }
        }
    }
}