package com.kevinfreyap.product.presentation.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kevinfreyap.product.R
import com.kevinfreyap.product.domain.usecase.GetLowStockProductUseCase
import com.kevinfreyap.product.domain.usecase.GetRecentProductListUseCase
import com.kevinfreyap.product.domain.usecase.GetTotalProductCountUseCase
import com.kevinfreyap.product.presentation.mapper.toUiModel
import com.kevinfreyap.product.presentation.model.AlertListUi
import com.kevinfreyap.product.presentation.state.DashboardState
import com.kevinfreyap.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getTotalProductCount: GetTotalProductCountUseCase,
    private val getRecentProductList: GetRecentProductListUseCase,
    private val getLowStockProduct: GetLowStockProductUseCase
): ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val alertListFlow: Flow<AlertListUi> = getLowStockProduct()
        .flatMapLatest { lowStock ->
            if (lowStock.isNotEmpty()) {
                val uiProducts = lowStock.take(3).toUiModel()

                val alertListUi = AlertListUi(
                    title = R.string.label_low_stock_product,
                    textButton = R.string.btn_label_view_all_low_stock_product,
                    products = uiProducts,
                    textButtonArg = lowStock.size
                )

                flowOf(alertListUi)
            } else {
                getRecentProductList()
                    .map { recentList ->
                        val uiProducts = recentList.toUiModel()

                        AlertListUi(
                            title = R.string.label_recently_updated,
                            textButton = R.string.btn_label_view_all_product,
                            products = uiProducts,
                            textButtonArg = null
                        )
                    }
            }
        }

    val uiState: StateFlow<UiState<DashboardState>> = combine(
        flow = getTotalProductCount(),
        flow2 = alertListFlow
    ) { totalProductCount, alertList ->
        if (totalProductCount == 0) return@combine UiState.Empty

        UiState.Success(
            DashboardState(
                totalProductCount = totalProductCount,
                lowStockProductCount = alertList.products.size,
                alertList = alertList
            )
        )
    }
        .catch { exception ->
            emit(UiState.Error(exception.message ?: "An unexpected error occurred"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )
}