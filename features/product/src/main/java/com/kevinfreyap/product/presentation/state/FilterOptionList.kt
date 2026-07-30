package com.kevinfreyap.product.presentation.state

import com.kevinfreyap.product.presentation.model.CategoryUi
import com.kevinfreyap.product.presentation.model.LocationUi

data class FilterOptionList(
    val categories: List<CategoryUi> = emptyList(),
    val locations: List<LocationUi> = emptyList(),
)