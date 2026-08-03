package com.kevinfreyap.product.presentation.model

import androidx.annotation.StringRes

data class AlertListUi(
    @param:StringRes val title: Int,
    @param:StringRes val textButton: Int,
    val products: List<ProductListItemUi>,
    val textButtonArg: Int? = null
)
