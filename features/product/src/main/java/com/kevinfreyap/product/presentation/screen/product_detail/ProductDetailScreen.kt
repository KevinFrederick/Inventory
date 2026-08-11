package com.kevinfreyap.product.presentation.screen.product_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kevinfreyap.product.presentation.model.StockBatchUi
import com.kevinfreyap.product.presentation.screen.product_detail.section.SectionBarcode
import com.kevinfreyap.product.presentation.screen.product_detail.section.SectionBarcodePlaceholder
import com.kevinfreyap.product.presentation.screen.product_detail.section.SectionBatches
import com.kevinfreyap.product.presentation.screen.product_detail.section.SectionBatchesPlaceholder
import com.kevinfreyap.product.presentation.screen.product_detail.section.SectionDescription
import com.kevinfreyap.product.presentation.screen.product_detail.section.SectionDescriptionPlaceholder
import com.kevinfreyap.product.presentation.screen.product_detail.section.SectionMinQtyAndExpDate
import com.kevinfreyap.product.presentation.screen.product_detail.section.SectionMinQtyAndExpDatePlaceholder
import com.kevinfreyap.product.presentation.screen.product_detail.section.SectionProductCore
import com.kevinfreyap.product.presentation.screen.product_detail.section.SectionProductCorePlaceholder
import com.kevinfreyap.product.presentation.screen.product_detail.section.SectionTotalQtyAndPriceRange
import com.kevinfreyap.product.presentation.screen.product_detail.section.SectionTotalQtyAndPriceRangePlaceholder
import com.kevinfreyap.product.presentation.state.ProductDetailState
import com.kevinfreyap.ui.R as coreR
import com.kevinfreyap.product.R
import com.kevinfreyap.product.presentation.navigation.ProductDetailNavigation
import com.kevinfreyap.ui.components.AppCenterTopBar
import com.kevinfreyap.ui.components.AppImageCard
import com.kevinfreyap.ui.components.AppImageCardPlaceholder
import com.kevinfreyap.ui.components.AppPrimaryButton
import com.kevinfreyap.ui.components.AppStateBanner
import com.kevinfreyap.ui.state.UiState
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun ProductDetailScreen(
    modifier: Modifier = Modifier,
    onNavigate:  (ProductDetailNavigation) -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProductDetailContent(
        uiState = uiState,
        onNavigate = onNavigate,
        modifier = modifier
    )
}

@Composable
fun ProductDetailContent(
    uiState: UiState<ProductDetailState>,
    onNavigate: (ProductDetailNavigation) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            AppCenterTopBar(
                title = stringResource(R.string.title_product_detail),
                onBackClick = {
                    onNavigate(ProductDetailNavigation.NavigateUp)
                },
                isLoading = false,
                actionButton = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(coreR.drawable.delete_24),
                            contentDescription = "Delete product",
                            tint = Theme.custom.primaryText,
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    painter = painterResource(coreR.drawable.edit_24),
                    contentDescription = "Edit Product"
                )
            }
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            when(uiState) {
                UiState.Idle -> {}
                UiState.Empty -> {
                    ProductDetailEmptyOrError (
                        isError = false,
                        onNavigate = onNavigate
                    )
                }
                UiState.Loading -> {
                    ProductDetailShimmer()
                }
                is UiState.Success -> {
                    ProductDetailSuccess(
                        productDetailState = uiState.data
                    )
                }
                is UiState.Error -> {
                    ProductDetailEmptyOrError(
                        isError = true,
                        onNavigate = onNavigate,
                        onRetry = {}
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductDetailShimmer() {
    AppImageCardPlaceholder()
    SectionProductCorePlaceholder()
    SectionTotalQtyAndPriceRangePlaceholder()
    SectionMinQtyAndExpDatePlaceholder()
    SectionDescriptionPlaceholder()
    SectionBatchesPlaceholder()
    SectionBarcodePlaceholder()
}

@Composable
private fun ProductDetailSuccess(
    productDetailState: ProductDetailState
) {
    AppImageCard(imageUri = productDetailState.imageUri)

    SectionProductCore(
        name = productDetailState.productName,
        sku = productDetailState.productSku,
        category = productDetailState.productCategory,
    )

    SectionTotalQtyAndPriceRange(
        stockStatus = stringResource(productDetailState.stockStatus),
        quantity = productDetailState.productTotalQtyText,
        minPrice = productDetailState.productMinPrice,
        maxPrice = productDetailState.productMaxPrice,
    )

    SectionMinQtyAndExpDate(
        minQuantity = productDetailState.productMinQtyText,
        expirationText = productDetailState.productNearestExpDate,
        modifier = Modifier
            .padding(horizontal = 8.dp)
    )

    SectionDescription(
        description = productDetailState.productDescription
    )

    SectionBatches(
        batches = productDetailState.batchesPreviewList,
        totalBatchCount = productDetailState.totalBatchCount,
        onSeeAllBatchClick = {  },
        onBatchItemClick = {  },
    )

    SectionBarcode(
        barcodeText = productDetailState.productBarcode
    )
}

@Composable
private fun ProductDetailEmptyOrError(
    isError: Boolean,
    onNavigate: (ProductDetailNavigation) -> Unit,
    onRetry: (() -> Unit)? = null
) {
    val icon = if (isError) R.drawable.custom_error_load_icon else R.drawable.custom_no_result_icon
    val title = if (isError) R.string.title_banner_something_wrong else R.string.title_banner_not_found
    val subtitle = if (isError) R.string.subtitle_banner_something_wrong else R.string.subtitle_banner_not_found

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .heightIn(min = minHeight)
        ) {
            AppStateBanner(
                bannerIcon = icon,
                bannerTitle = stringResource(title),
                bannerSubtitle = stringResource(subtitle),
                actionButton = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (isError) {
                            AppPrimaryButton(
                                text = stringResource(R.string.btn_label_try_again),
                                onClick = { onRetry?.invoke() },
                                icon = {
                                    Icon(
                                        painter = painterResource(coreR.drawable.refresh_24),
                                        contentDescription = stringResource(R.string.btn_label_try_again),
                                    )
                                },
                            )
                        }

                        AppPrimaryButton(
                            text = stringResource(R.string.btn_label_go_back),
                            onClick = {
                                onNavigate(ProductDetailNavigation.NavigateUp)
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(coreR.drawable.arrow_left_24),
                                    contentDescription = stringResource(R.string.btn_label_go_back),
                                )
                            },
                        )
                    }
                }
            )
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1080px,height=3340px,dpi=416",
)
@Composable
fun ProductDetailScreenPreview() {
    InventoryTheme {
        ProductDetailContent(
            onNavigate = {},
            uiState = UiState.Success(
                ProductDetailState(
                    imageUri = "",
                    productName = "Something Very Long Text",
                    productCategory = "Electronic",
                    productSku = "#SKU-1234-B",
                    productMinQty = 5,
                    productNearestExpDate = null,
                    productDescription = LoremIpsum(words = 50).values.first(),
                    productBarcode = "1234567890",
                    batchesPreviewList = listOf(
                        StockBatchUi(
                            id = "batch-550e84",
                            quantity = 2,
                            price = "Rp 1.000.000",
                            location = "Garage",
                            expDate = "31 September 2020",
                            supplier = null
                        ),
                        StockBatchUi(
                            id = "batch-50e84a",
                            quantity = 3,
                            price = "",
                            location = "Garage",
                            expDate = "31 September 2020",
                            supplier = null
                        ),
                        StockBatchUi(
                            id = "batch-0e84b3",
                            quantity = 2,
                            price = "Rp 1.000.000",
                            location = "Garage",
                            expDate = "31 September 2020",
                            supplier = null
                        ),
                    ),
                    batchesList = listOf(
                        StockBatchUi(
                            id = "batch-550e84",
                            quantity = 2,
                            price = "Rp 1.000.000",
                            location = "Garage",
                            expDate = "31 September 2020",
                            supplier = null
                        ),
                        StockBatchUi(
                            id = "batch-50e84a",
                            quantity = 3,
                            price = "",
                            location = "Garage",
                            expDate = "31 September 2020",
                            supplier = null
                        ),
                        StockBatchUi(
                            id = "batch-0e84b3",
                            quantity = 2,
                            price = "Rp 1.000.000",
                            location = "Garage",
                            expDate = "31 September 2020",
                            supplier = null
                        ),
                    ),
                    totalBatchCount = 7
                )
            )
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1080px,height=3340px,dpi=416",
)
@Composable
fun ProductDetailScreenPreview_Loading() {
    InventoryTheme {
        ProductDetailContent(
            uiState = UiState.Loading,
            onNavigate = {}
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1080px,height=3340px,dpi=416",
)
@Composable
fun ProductDetailScreenPreview_Empty() {
    InventoryTheme {
        ProductDetailContent(
            uiState = UiState.Empty,
            onNavigate = {}
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1080px,height=3340px,dpi=416",
)
@Composable
fun ProductDetailScreenPreview_Error() {
    InventoryTheme {
        ProductDetailContent(
            uiState = UiState.Error(""),
            onNavigate = {}
        )
    }
}