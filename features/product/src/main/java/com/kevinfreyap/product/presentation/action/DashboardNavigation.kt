package com.kevinfreyap.product.presentation.action

sealed interface DashboardNavigation {
    object AddProduct: DashboardNavigation
    object LowStockProduct: DashboardNavigation
    object AllProduct: DashboardNavigation
    data class ProductDetail(val productId: String): DashboardNavigation

}