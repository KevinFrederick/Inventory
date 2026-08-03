package com.kevinfreyap.product.presentation.screen.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kevinfreyap.product.R
import com.kevinfreyap.product.presentation.action.DashboardNavigation
import com.kevinfreyap.product.presentation.mapper.color
import com.kevinfreyap.product.presentation.model.AlertListUi
import com.kevinfreyap.product.presentation.model.ProductListItemUi
import com.kevinfreyap.product.presentation.model.StockLevel
import com.kevinfreyap.product.presentation.screen.dashboard.section.SectionCountRow
import com.kevinfreyap.product.presentation.screen.dashboard.section.SectionGreetings
import com.kevinfreyap.product.presentation.screen.dashboard.section.SectionListWithHeader
import com.kevinfreyap.product.presentation.state.DashboardState
import com.kevinfreyap.ui.components.AppBaseListItem
import com.kevinfreyap.ui.R as coreR
import com.kevinfreyap.ui.components.AppIconName
import com.kevinfreyap.ui.components.AppPrimaryButton
import com.kevinfreyap.ui.components.AppStateBanner
import com.kevinfreyap.ui.state.UiState
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun DashboardScreen(
    onNavigate: (DashboardNavigation) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardContent(
        state = state,
        onNavigate = onNavigate,
        modifier = modifier
    )
}

@Composable
fun DashboardContent(
    state: UiState<DashboardState>,
    onNavigate: (DashboardNavigation) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                AppIconName()

                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Transparent
                    ),
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(coreR.drawable.search_24),
                        contentDescription = "Search Icon",
                        tint = Theme.custom.primaryText
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .padding(innerPadding)
                .padding(
                    top = 12.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            when (state) {
                UiState.Empty -> {
                    SectionGreetings(
                        title = "Good Morning, User",
                        subtitle = "Let's set up your inventory"
                    )

                    SectionCountRow(
                        totalProductValue = 0,
                        lowStockValue = 0
                    )

                    AppStateBanner(
                        bannerIcon = R.drawable.custom_empty_storage_icon,
                        bannerTitle = stringResource(R.string.title_banner_empty_inventory),
                        bannerSubtitle = stringResource(R.string.subtitle_banner_empty_inventory),
                        actionButton = {
                            AppPrimaryButton(
                                text = stringResource(R.string.btn_label_add_first_item),
                                onClick = {
                                    onNavigate(DashboardNavigation.AddProduct)
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(coreR.drawable.add_24),
                                        contentDescription = stringResource(R.string.btn_label_add_first_item),
                                    )
                                },
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )
                }
                UiState.Loading -> {
                    DashboardShimmer()
                }
                is UiState.Error -> {
                    SectionGreetings(
                        title = "Good Morning, User",
                        subtitle = ""
                    )

                    AppStateBanner(
                        bannerIcon = R.drawable.custom_error_load_icon,
                        bannerTitle = stringResource(R.string.title_banner_load_error),
                        bannerSubtitle = stringResource(R.string.subtitle_banner_load_error),
                        actionButton = {
                            AppPrimaryButton(
                                text = stringResource(R.string.btn_label_try_again),
                                onClick = {

                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(coreR.drawable.refresh_24),
                                        contentDescription = stringResource(R.string.btn_label_try_again),
                                    )
                                },
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )
                }
                is UiState.Success -> {
                    val listData = state.data.alertList

                    SectionGreetings(
                        title = "Good Morning, User",
                        subtitle = "Data last synced at 09:11"
                    )

                    SectionCountRow(
                        totalProductValue = state.data.totalProductCount,
                        lowStockValue = state.data.lowStockProductCount,
                    )

                    SectionListWithHeader(
                        title = stringResource(listData.title),
                        list = listData.products,
                        textButton = {
                            Text(
                                text = listData.textButtonArg?.let { arg ->
                                    stringResource(listData.textButton, arg)
                                } ?: stringResource(listData.textButton),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(50))
                                    .clickable  {

                                    }
                                    .padding(
                                        vertical = 8.dp,
                                    )
                            )
                        }
                    ) { product ->
                        AppBaseListItem(
                            itemName = product.name,
                            subtitle = if (product.sku != null) {
                                {
                                    Text(
                                        text = product.sku,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Theme.custom.secondaryText
                                    )
                                }
                            } else null,
                            trailingData = {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .padding(4.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.label_quantity),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Theme.custom.secondaryText,
                                    )
                                    Text(
                                        text = product.quantity.toString(),
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Medium,
                                        color = product.stockLevel.color
                                    )
                                }
                            },
                            onClick = {
                                onNavigate(DashboardNavigation.ProductDetail(product.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1080px,height=2340px,dpi=416",
    showSystemUi = true
)
@Composable
fun DashboardScreenPreview() {
    InventoryTheme {
        DashboardContent(
            state = UiState.Success(
                DashboardState(
                    totalProductCount = 1700,
                    lowStockProductCount = 7,
                    alertList = AlertListUi(
                        title = R.string.label_low_stock_product,
                        textButton = R.string.btn_label_view_all_low_stock_product,
                        products = listOf(
                            ProductListItemUi(
                                id = "PROD-01",
                                name = "Smartphone",
                                quantity = 100,
                                stockLevel = StockLevel.IN_STOCK,
                                category = "Electronic",
                                imageUri = null,
                                sku = "#SKU-1234-B"
                            ),
                            ProductListItemUi(
                                id = "PROD-02",
                                name = "Smartphone 2",
                                quantity = 10,
                                stockLevel = StockLevel.LOW_STOCK,
                                category = "Electronic",
                                imageUri = null,
                                sku = "#SKU-1234-B"
                            ),
                            ProductListItemUi(
                                id = "PROD-03",
                                name = "Smartphone 3",
                                quantity = 0,
                                stockLevel = StockLevel.OUT_OF_STOCK,
                                category = "Electronic",
                                imageUri = null,
                                sku = "#SKU-1234-B"
                            ),
                        ),
                        textButtonArg = 7
                    )
                )
            ),
//            state = UiState.Empty,
//            state = UiState.Error(""),
            onNavigate = {}
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1080px,height=2340px,dpi=416",
    showSystemUi = true
)
@Composable
fun DashboardScreenPreview_StateLoading() {
    InventoryTheme {
        DashboardContent(
            state = UiState.Loading,
//            state = UiState.Empty,
//            state = UiState.Error(""),
            onNavigate = {}
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1080px,height=2340px,dpi=416",
    showSystemUi = true
)
@Composable
fun DashboardScreenPreview_StateEmpty() {
    InventoryTheme {
        DashboardContent(
            state = UiState.Empty,
            onNavigate = {}
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1080px,height=2340px,dpi=416",
    showSystemUi = true
)
@Composable
fun DashboardScreenPreview_StateError() {
    InventoryTheme {
        DashboardContent(
            state = UiState.Error(""),
            onNavigate = {}
        )
    }
}