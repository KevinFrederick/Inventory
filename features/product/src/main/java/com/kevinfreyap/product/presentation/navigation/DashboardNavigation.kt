package com.kevinfreyap.product.presentation.navigation

sealed interface DashboardNavigation {
    object AddProduct: DashboardNavigation
    object AllProduct: DashboardNavigation
    data class LowStockProduct(val stockFilter: String? = null): DashboardNavigation
    data class ProductDetail(val productId: String): DashboardNavigation

}