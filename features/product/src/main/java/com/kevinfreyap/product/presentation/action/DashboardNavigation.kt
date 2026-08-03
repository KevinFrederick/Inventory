package com.kevinfreyap.product.presentation.action

sealed interface DashboardNavigation {
    object AddProduct: DashboardNavigation
    data class ProductDetail(val productId: String): DashboardNavigation

}