package com.kevinfreyap.inventory

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.kevinfreyap.product.presentation.action.ProductListNavigation
import com.kevinfreyap.product.presentation.screen.product_list.ProductListScreen

@Composable
fun MainAppScreen(
    startDestination: String,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost (
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
                .padding(innerPadding)
        ) {
            composable(Screen.ProductList.route) {
                ProductListScreen(
                    onNavigate = { destination ->
                        when(destination) {
                            is ProductListNavigation.AddProduct -> {
                                navController.navigate(Screen.AddProduct.route)
                            }
                            is ProductListNavigation.ProductDetail -> {
                                navController.navigate(
                                    Screen.ProductDetail.createRoute(destination.productId)
                                )
                            }
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute =navBackStackEntry?.destination?.route

        val navigationItems = listOf(
            BottomTabItem(
                route = Screen.Dashboard.route,
                title = stringResource(R.string.bottom_tab_dashboard),
                icon = R.drawable.dashboard_24
            ),
            BottomTabItem(
                route = Screen.ProductList.route,
                title = stringResource(R.string.bottom_tab_product_list),
                icon = R.drawable.format_list_bulleted_24
            )
        )

        navigationItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                icon = {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(item.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                ),
                onClick = {
                    navController.navigate(item.route){
                        val startRoute = navController.graph.startDestinationRoute
                            ?: Screen.Dashboard.route

                        popUpTo(startRoute){
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}