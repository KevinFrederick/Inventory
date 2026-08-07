package com.kevinfreyap.product.presentation.navigation

sealed interface AddProductNavigation {
    object NavigateUp: AddProductNavigation
}