package com.kevinfreyap.product.presentation.state

import com.kevinfreyap.product.presentation.model.AlertListUi

data class ScreenDashboardState(
    val totalProductCount: Int = 0,
    val lowStockProductCount: Int = 0,
    val alertList: AlertListUi
)
