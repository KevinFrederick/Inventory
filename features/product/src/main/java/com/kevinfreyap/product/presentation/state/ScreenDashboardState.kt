package com.kevinfreyap.product.presentation.state

import com.kevinfreyap.product.presentation.model.AlertListUi

data class ScreenDashboardState(
    val totalProductCount: String = "0",
    val totalItemsCount: String = "0",
    val estimatedValue: String? = null,
    val alertList: AlertListUi
)
