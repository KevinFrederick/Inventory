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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.kevinfreyap.domain.model.InventoryBarcode
import com.kevinfreyap.domain.util.SCANNED_BARCODE
import com.kevinfreyap.domain.util.SCANNED_FORMAT
import com.kevinfreyap.product.presentation.navigation.AddProductNavigation
import com.kevinfreyap.product.presentation.navigation.BatchFormNavigation
import com.kevinfreyap.product.presentation.navigation.DashboardNavigation
import com.kevinfreyap.product.presentation.navigation.EditProductNavigation
import com.kevinfreyap.product.presentation.navigation.ProductDetailNavigation
import com.kevinfreyap.product.presentation.navigation.ProductListNavigation
import com.kevinfreyap.product.presentation.navigation.ProductScreen
import com.kevinfreyap.product.presentation.screen.add_product.AddProductScreen
import com.kevinfreyap.product.presentation.screen.batch_form.BatchFormScreen
import com.kevinfreyap.product.presentation.screen.dashboard.DashboardScreen
import com.kevinfreyap.product.presentation.screen.edit_product.EditProductScreen
import com.kevinfreyap.product.presentation.screen.product_detail.ProductDetailScreen
import com.kevinfreyap.product.presentation.screen.product_list.ProductListScreen
import com.kevinfreyap.scanner.navigation.ScannerNavigation
import com.kevinfreyap.scanner.navigation.ScannerScreens
import com.kevinfreyap.scanner.ui.ScannerScreen

@Composable
fun MainAppScreen(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.let { destination ->
        destination.hasRoute<ProductScreen.Dashboard>() ||
        destination.hasRoute<ProductScreen.ProductList>()
    } ?: false

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost (
            navController = navController,
            startDestination = ProductScreen.Dashboard,
            modifier = modifier
                .padding(innerPadding)
        ) {
            composable<ProductScreen.Dashboard> { entry ->
                val scannedBarcode by entry.savedStateHandle
                    .getStateFlow(SCANNED_BARCODE, "")
                    .collectAsStateWithLifecycle()

                val scannedFormat by entry.savedStateHandle
                    .getStateFlow(SCANNED_FORMAT, "")
                    .collectAsStateWithLifecycle()

                val inventoryBarcode = InventoryBarcode(
                    value = scannedBarcode,
                    format = scannedFormat
                )

                DashboardScreen(
                    scannedBarcode = inventoryBarcode,
                    onClearBarcode = {
                        entry.savedStateHandle[SCANNED_BARCODE] = ""
                        entry.savedStateHandle[SCANNED_FORMAT] = ""
                    },
                    onNavigate = {destination ->
                        when(destination) {
                            is DashboardNavigation.AddProduct -> {
                                navController.navigate(
                                    ProductScreen.AddProduct(
                                        barcodeValue = destination.barcodeValue,
                                        barcodeFormat = destination.barcodeFormat
                                    )
                                )
                            }
                            is DashboardNavigation.BarcodeScanner -> {
                                navController.navigate(ScannerScreens.ScannerScreen)
                            }
                            is DashboardNavigation.ProductDetail -> {
                                navController.navigate(ProductScreen.ProductDetail(productId = destination.productId))
                            }
                            is DashboardNavigation.ProductList -> {
                                val filter = destination.filter
                                navController.navigate(ProductScreen.ProductList(filter = filter)) {
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

            composable<ProductScreen.ProductList> {
                ProductListScreen(
                    onNavigate = { destination ->
                        when(destination) {
                            is ProductListNavigation.AddProduct -> {
                                navController.navigate(ProductScreen.AddProduct())
                            }
                            is ProductListNavigation.ProductDetail -> {
                                navController.navigate(
                                    ProductScreen.ProductDetail(destination.productId)
                                )
                            }
                        }
                    },
                )
            }

            composable<ProductScreen.AddProduct> { entry ->
                val scannedBarcode by entry.savedStateHandle
                    .getStateFlow(SCANNED_BARCODE, "")
                    .collectAsStateWithLifecycle()

                val scannedFormat by entry.savedStateHandle
                    .getStateFlow(SCANNED_FORMAT, "")
                    .collectAsStateWithLifecycle()

                val inventoryBarcode = InventoryBarcode(
                    value = scannedBarcode,
                    format = scannedFormat
                )

                AddProductScreen(
                    scannedBarcode = inventoryBarcode,
                    onClearBarcode = {
                        entry.savedStateHandle[SCANNED_BARCODE] = ""
                        entry.savedStateHandle[SCANNED_FORMAT] = ""
                    },
                    onNavigate = { destination ->
                        when (destination) {
                            AddProductNavigation.NavigateUp -> {
                                navController.navigateUp()
                            }
                            AddProductNavigation.BarcodeScanner -> {
                                navController.navigate(
                                    ScannerScreens.ScannerScreen
                                )
                            }
                        }
                    }
                )
            }

            composable<ProductScreen.ProductDetail> {
                ProductDetailScreen(
                    onNavigate = { destination ->
                        when (destination) {
                            is ProductDetailNavigation.NavigateUp -> {
                                navController.navigateUp()
                            }

                            is ProductDetailNavigation.EditProduct -> {
                                navController.navigate(
                                    ProductScreen.EditProduct(destination.productId)
                                )
                            }

                            is ProductDetailNavigation.AddBatch -> {
                                val productId = destination.productId

                                navController.navigate(
                                    ProductScreen.BatchForm(
                                        productId = productId,
                                        batchId = null
                                    )
                                )
                            }

                            is ProductDetailNavigation.BatchDetail -> {
                                val productId = destination.productId
                                val batchId = destination.batchId

                                navController.navigate(
                                    ProductScreen.BatchForm(
                                        productId = productId,
                                        batchId = batchId
                                    )
                                )
                            }
                        }
                    }
                )
            }

            composable<ProductScreen.EditProduct> { entry ->
                val scannedBarcode by entry.savedStateHandle
                    .getStateFlow(SCANNED_BARCODE, "")
                    .collectAsStateWithLifecycle()

                val scannedFormat by entry.savedStateHandle
                    .getStateFlow(SCANNED_FORMAT, "")
                    .collectAsStateWithLifecycle()

                val inventoryBarcode = InventoryBarcode(
                    value = scannedBarcode,
                    format = scannedFormat
                )

                EditProductScreen(
                    scannedBarcode = inventoryBarcode,
                    onClearBarcode = {
                        entry.savedStateHandle[SCANNED_BARCODE] = ""
                        entry.savedStateHandle[SCANNED_FORMAT] = ""
                    },
                    onNavigate = {destination ->
                        when (destination) {
                            EditProductNavigation.NavigateUp -> {
                                navController.navigateUp()
                            }
                            EditProductNavigation.BarcodeScanner -> {
                                navController.navigate(
                                    ScannerScreens.ScannerScreen
                                )
                            }
                        }
                    }
                )
            }

            composable<ProductScreen.BatchForm> {
                BatchFormScreen(
                    onNavigate = { destination ->
                        when(destination) {
                            BatchFormNavigation.NavigateUp -> {
                                navController.navigateUp()
                            }
                        }
                    }
                )
            }
            
            composable<ScannerScreens.ScannerScreen> {
                ScannerScreen(
                    onScanSuccess = { barcode, format ->
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.apply {
                                set(SCANNED_BARCODE, barcode)
                                set(SCANNED_FORMAT, format)
                            }

                        navController.popBackStack()
                    },
                    onNavigate = { destination ->
                        when(destination) {
                            ScannerNavigation.NavigateUp -> {
                                navController.popBackStack()
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
        val currentDestination = navBackStackEntry?.destination

        val navigationItems = listOf(
            BottomTabItem(
                route = ProductScreen.Dashboard,
                title = stringResource(R.string.bottom_tab_dashboard),
                icon = R.drawable.dashboard_24
            ),
            BottomTabItem(
                route = ProductScreen.ProductList(filter = null),
                title = stringResource(R.string.bottom_tab_product_list),
                icon = R.drawable.format_list_bulleted_24
            )
        )

        navigationItems.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.hasRoute(item.route::class) == true,
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
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true
                    }
                },
            )
        }
    }
}