package com.kevinfreyap.inventory

import androidx.annotation.DrawableRes

data class BottomTabItem (
    val route: String,
    val title: String,
    @param:DrawableRes val icon: Int
)