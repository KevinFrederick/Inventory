package com.kevinfreyap.product.presentation.screen.product_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.kevinfreyap.product.R
import com.kevinfreyap.product.presentation.action.FilterQueryAction
import com.kevinfreyap.product.presentation.action.ProductListNavigation
import com.kevinfreyap.product.presentation.mapper.color
import com.kevinfreyap.ui.R as coreR
import com.kevinfreyap.product.presentation.model.ProductListItemUi
import com.kevinfreyap.product.presentation.model.StockLevel
import com.kevinfreyap.product.presentation.screen.filter.FilterBottomSheet
import com.kevinfreyap.product.presentation.state.FilterOptionList
import com.kevinfreyap.product.presentation.state.FilterState
import com.kevinfreyap.ui.components.AppBaseListItem
import com.kevinfreyap.ui.components.AppBaseListItemPlaceholder
import com.kevinfreyap.ui.components.AppPrimaryButton
import com.kevinfreyap.ui.components.AppSearchBar
import com.kevinfreyap.ui.components.AppStateBanner
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import kotlinx.coroutines.flow.flowOf

@Composable
fun ProductListScreen(
    onNavigate: (ProductListNavigation) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductListViewModel = hiltViewModel()
) {
    val products = viewModel.productFlow.collectAsLazyPagingItems()

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val availableFilterOption by viewModel.availableFilters.collectAsStateWithLifecycle()
    val draftFilter by viewModel.draftFilter.collectAsStateWithLifecycle()

    var showFilterSheet by remember { mutableStateOf(false) }

    ProductListContent(
        products = products,
        searchQuery = searchQuery,
        availableFilterOption = availableFilterOption,
        draftFilter = draftFilter,
        showFilterSheet = showFilterSheet,
        onToggleFilterSheet = { isVisible ->
            showFilterSheet = isVisible
        },
        onFilterAction = viewModel::onFilterAction,
        onNavigate = onNavigate,
        modifier = modifier
    )
}

@Composable
fun ProductListContent(
    products: LazyPagingItems<ProductListItemUi>,
    searchQuery: String,
    availableFilterOption: FilterOptionList,
    draftFilter: FilterState,
    showFilterSheet: Boolean,
    onToggleFilterSheet: (Boolean) -> Unit,
    onFilterAction: (FilterQueryAction) -> Unit,
    onNavigate: (ProductListNavigation) -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(top = 0.dp)
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                AppSearchBar(
                    searchQuery = searchQuery,
                    placeholder = stringResource(R.string.placeholder_search_inventory),
                    onQueryChange = { query ->
                        onFilterAction(FilterQueryAction.UpdateSearchQuery(query))
                    },
                    modifier = Modifier
                        .weight(1f)
                )

                IconButton(
                    onClick = {
                        onFilterAction(FilterQueryAction.ShowAppliedFilter)
                        onToggleFilterSheet(true)
                    },
                    modifier = Modifier
                        .padding(4.dp)
                ) {
                    BadgedBox(
                        badge = { if (draftFilter.hasActiveFilter) Badge() },
                        modifier = modifier
                            .padding(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.filter_list_24),
                            contentDescription = "Filter Icon",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            when (products.loadState.refresh) {
                LoadState.Loading -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(count = 10) {
                            AppBaseListItemPlaceholder()
                        }
                    }
                }
                is LoadState.NotLoading -> {
                    if (products.itemCount == 0) {
                        val isSearchOrFilterActive = searchQuery.isNotEmpty() || draftFilter.hasActiveFilter

                        if (isSearchOrFilterActive) {
                            AppStateBanner(
                                bannerIcon = R.drawable.custom_no_result_icon,
                                bannerTitle = stringResource(R.string.title_banner_no_result),
                                bannerSubtitle = stringResource(R.string.subtitle_banner_no_result),
                                actionButton = {
                                    AppPrimaryButton(
                                        text = stringResource(R.string.btn_label_clear_filters),
                                        onClick = {
                                            onFilterAction(FilterQueryAction.ClearAll)
                                            onFilterAction(FilterQueryAction.UpdateSearchQuery(""))
                                        },
                                        icon = {
                                            Icon(
                                                painter = painterResource(coreR.drawable.delete_24),
                                                contentDescription = stringResource(R.string.btn_label_clear_filters),
                                            )
                                        },
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                        } else {
                            AppStateBanner(
                                bannerIcon = R.drawable.custom_empty_storage_icon,
                                bannerTitle = stringResource(R.string.title_banner_empty_inventory),
                                bannerSubtitle = stringResource(R.string.subtitle_banner_empty_inventory),
                                actionButton = {
                                    AppPrimaryButton(
                                        text = stringResource(R.string.btn_label_add_first_item),
                                        onClick = {
                                            onNavigate(ProductListNavigation.AddProduct)
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
                                    .fillMaxSize()
                            )
                        }

                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            items(
                                count = products.itemCount,
                                key = products.itemKey { item -> item.id }
                            ) { index ->
                                val product = products[index]
                                if (product != null) {
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
                                            onNavigate(ProductListNavigation.ProductDetail(product.id))
                                        }
                                    )
                                }
                            }

                            when (products.loadState.append) {
                                LoadState.Loading -> {
                                    item {
                                        Box(
                                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                                        }
                                    }
                                }
                                is LoadState.Error -> {
                                    item {
                                        TextButton(
                                            onClick = { products.retry() },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                        ) {
                                            Text(
                                                text = stringResource(R.string.txt_btn_label_tap_to_retry_load_more),
                                            )
                                        }
                                    }
                                }
                                is LoadState.NotLoading -> Unit
                            }
                        }
                    }
                }
                is LoadState.Error -> {
                    AppStateBanner(
                        bannerIcon = R.drawable.custom_error_load_icon,
                        bannerTitle = stringResource(R.string.title_banner_load_error),
                        bannerSubtitle = stringResource(R.string.subtitle_banner_load_error),
                        actionButton = {
                            AppPrimaryButton(
                                text = stringResource(R.string.btn_label_try_again),
                                onClick = {
                                    products.retry()
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
                            .fillMaxSize()
                    )
                }
            }
        }
    }

    if (showFilterSheet) {
        FilterBottomSheet(
            filterOptionList = availableFilterOption,
            filterState = draftFilter,
            onAction = { action ->
                onFilterAction(action)

                if (action is FilterQueryAction.ApplyFilter) {
                    onToggleFilterSheet(false)
                }
            },
            onDismiss = {
                onToggleFilterSheet(false)
            },
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1080px,height=2340px,dpi=416",
    showSystemUi = true
)
@Composable
fun ProductListPreview() {
    val sampleProducts = listOf(
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
    )

    val loadStates = LoadStates(
//        refresh = LoadState.Error(error = Throwable()),
        refresh = LoadState.NotLoading(endOfPaginationReached = true),
        prepend = LoadState.NotLoading(endOfPaginationReached = true),
        append = LoadState.NotLoading(endOfPaginationReached = true)
    )

    val pagingData = PagingData.from(
//        data = emptyList<ProductListItemUi>(),
        data = sampleProducts,
        sourceLoadStates = loadStates
    )

    // Turn into LazyPagingItems for preview
    val lazyPagingItems = flowOf(pagingData).collectAsLazyPagingItems()

    InventoryTheme {
        ProductListContent(
            products = lazyPagingItems,
            searchQuery = "",
            availableFilterOption = FilterOptionList(),
            draftFilter = FilterState(),
            showFilterSheet = false,
            onToggleFilterSheet = { },
            onFilterAction = {},
            onNavigate = {}
        )
    }
}
