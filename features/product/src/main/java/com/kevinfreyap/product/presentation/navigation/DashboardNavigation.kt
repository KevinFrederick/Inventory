package com.kevinfreyap.product.presentation.navigation

sealed interface DashboardNavigation {
    object AddProduct: DashboardNavigation
    data class ProductList(val filter: String? = null): DashboardNavigation
    data class ProductDetail(val productId: String): DashboardNavigation

}