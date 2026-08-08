package com.kevinfreyap.inventory

sealed class Screen(val route: String) {
    data object Dashboard: Screen("dashboard")
    data object ProductList: Screen("productList") {
        const val ROUTE_WITH_ARGS = "productList?stockFilter={stockFilter}"

        fun createRoute (stockFilter: String? = null): String {
            return if (stockFilter != null) {
                "productList?stockFilter=$stockFilter"
            } else {
                "productList"
            }
        }
    }
    data object AddProduct: Screen("addProduct")
    data object ProductDetail: Screen("productDetail/{productId}") {
        fun createRoute(productId: String) = "productDetail/$productId"
    }
}
