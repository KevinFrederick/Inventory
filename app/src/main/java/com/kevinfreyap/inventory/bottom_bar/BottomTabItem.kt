package com.kevinfreyap.inventory.bottom_bar

import androidx.annotation.DrawableRes

data class BottomTabItem <T: Any> (
    val route: T,
    val title: String,
    @param:DrawableRes val icon: Int
)