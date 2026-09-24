package com.kevinfreyap.product.presentation.navigation

sealed interface DashboardNavigation {
    object BarcodeScanner: DashboardNavigation
    data class AddProduct(val barcodeValue: String? = null, val barcodeFormat: String? = null): DashboardNavigation
    data class ProductList(val filter: String? = null): DashboardNavigation
    data class ProductDetail(val productId: String): DashboardNavigation

}