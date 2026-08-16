package com.kevinfreyap.inventory

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun RootAppNavigation(
    isLoggedIn: Boolean
) {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = if (isLoggedIn) AppGraph.Main else AppGraph.Auth
    ) {
        composable<AppGraph.Main> {
            MainAppScreen()
        }
    }
}