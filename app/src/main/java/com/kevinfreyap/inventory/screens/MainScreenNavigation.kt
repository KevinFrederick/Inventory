package com.kevinfreyap.inventory.screens

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kevinfreyap.domain.model.InventoryBarcode
import com.kevinfreyap.domain.util.SCANNED_BARCODE
import com.kevinfreyap.domain.util.SCANNED_FORMAT
import com.kevinfreyap.inventory.AppGraph
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

fun NavGraphBuilder.mainGraph(
    navController: NavHostController
) {
    navigation<AppGraph.Main>(
        startDestination = ProductScreen.Dashboard
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