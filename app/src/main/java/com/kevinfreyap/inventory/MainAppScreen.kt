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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kevinfreyap.product.presentation.navigation.AddProductNavigation
import com.kevinfreyap.product.presentation.navigation.DashboardNavigation
import com.kevinfreyap.product.presentation.navigation.ProductListNavigation
import com.kevinfreyap.product.presentation.screen.add_product.AddProductScreen
import com.kevinfreyap.product.presentation.screen.dashboard.DashboardScreen
import com.kevinfreyap.product.presentation.screen.product_list.ProductListScreen

@Composable
fun MainAppScreen(
    startDestination: String,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = listOf(
        Screen.Dashboard.route,
        Screen.ProductList.route,
        Screen.ProductList.ROUTE_WITH_ARGS
    )

    val showBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost (
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
                .padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onNavigate = {destination ->
                        when(destination) {
                            is DashboardNavigation.AddProduct -> {
                                navController.navigate(Screen.AddProduct.route)
                            }
                            is DashboardNavigation.ProductDetail -> {
                                navController.navigate(
                                    Screen.ProductDetail.createRoute(destination.productId)
                                )
                            }
                            is DashboardNavigation.AllProduct -> {
                                navController.navigate(Screen.ProductList.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            is DashboardNavigation.LowStockProduct -> {
                                val filter = destination.stockFilter
                                navController.navigate(Screen.ProductList.createRoute(stockFilter = filter)) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                )
            }

            composable(
                route = Screen.ProductList.ROUTE_WITH_ARGS,
                arguments = listOf(
                    navArgument(
                        "stockFilter"
                    ) {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) {
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

            composable(Screen.AddProduct.route) {
                AddProductScreen(
                    onNavigate = { destination ->
                        when (destination) {
                            AddProductNavigation.NavigateUp -> {
                                navController.navigateUp()
                            }
                        }
                    }
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
                route = Screen.ProductList.createRoute(stockFilter = null),
                title = stringResource(R.string.bottom_tab_product_list),
                icon = R.drawable.format_list_bulleted_24
            )
        )

        navigationItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute?.contains(item.route) == true,
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

                        popUpTo(startRoute)

                        launchSingleTop = true
                    }
                },
            )
        }
    }
}