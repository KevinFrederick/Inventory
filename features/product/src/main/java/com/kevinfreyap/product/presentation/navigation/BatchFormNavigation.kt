package com.kevinfreyap.product.presentation.navigation

sealed interface BatchFormNavigation {
    data object NavigateUp: BatchFormNavigation
}