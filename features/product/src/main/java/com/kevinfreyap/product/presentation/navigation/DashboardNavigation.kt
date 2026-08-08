package com.kevinfreyap.product.presentation.navigation

import com.kevinfreyap.product.presentation.state.FilterState

sealed interface DashboardNavigation {
    object AddProduct: DashboardNavigation
    object AllProduct: DashboardNavigation
    data class LowStockProduct(val stockFilter: String? = null): DashboardNavigation
    data class ProductDetail(val productId: String): DashboardNavigation

}