package com.kevinfreyap.product.presentation.navigation

sealed interface EditProductNavigation {
    object NavigateUp: EditProductNavigation
}