package com.kevinfreyap.inventory

sealed class Screen(val route: String) {
    data object Dashboard: Screen("dashboard")
    data object ProductList: Screen("productList")
    data object AddProduct: Screen("addProduct")
    data object ProductDetail: Screen("productDetail/{productId}") {
        fun createRoute(productId: String) = "productDetail/$productId"
    }
}
