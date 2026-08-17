package com.kevinfreyap.product.presentation.state

import com.kevinfreyap.product.presentation.model.AlertListUi

data class ScreenDashboardState(
    val totalProductRaw: Int = 0,
    val totalProductCount: String = "0",
    val totalItemsCount: String = "0",
    val estimatedValue: String? = null,
    val lowStockAlert: AlertListUi? = null,
    val recentProduct: AlertListUi
)
