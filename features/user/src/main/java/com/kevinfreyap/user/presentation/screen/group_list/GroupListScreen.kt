package com.kevinfreyap.user.presentation.screen.group_list

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.kevinfreyap.ui.components.AppBaseListItemPlaceholder
import com.kevinfreyap.ui.components.AppCenterTopBar
import com.kevinfreyap.ui.components.AppPrimaryButton
import com.kevinfreyap.ui.components.AppTextField
import com.kevinfreyap.ui.event.UiEvent
import com.kevinfreyap.ui.state.UiState
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.ui.util.shimmerEffect
import com.kevinfreyap.user.R
import com.kevinfreyap.user.presentation.action.GroupListAction
import com.kevinfreyap.user.presentation.components.GroupListItem
import com.kevinfreyap.user.presentation.navigation.GroupListNavigation
import com.kevinfreyap.user.presentation.state.GroupListItemState
import com.kevinfreyap.user.presentation.state.GroupListScreenState
import com.kevinfreyap.ui.R as coreR

@Composable
fun GroupListScreen(
    onNavigate: (GroupListNavigation) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GroupListViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.uiEvent, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEvent.collect { event ->
                when(event) {
                    is UiEvent.Navigate -> {}
                    is UiEvent.ShowToast -> {
                        Toast.makeText(
                            context,
                            event.asString(context),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }


    GroupListContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        onNavigate = onNavigate,
        modifier = modifier
    )
}

@Composable
fun GroupListContent(
    uiState: UiState<GroupListScreenState>,
    onAction: (GroupListAction) -> Unit,
    onNavigate: (GroupListNavigation) -> Unit,
    modifier: Modifier = Modifier
) {
    var cachedData by remember { mutableStateOf<GroupListScreenState?>(null) }
    if (uiState is UiState.Success) cachedData = uiState.data

    Scaffold(
        topBar = {
            AppCenterTopBar(
                title = "",
                onBackClick = {
                    onNavigate(GroupListNavigation.NavigateUp)
                },
                isLoading = uiState is UiState.Loading
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .padding(
                    top = 4.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .fillMaxSize()
        ) {
            when(uiState) {
                UiState.Empty -> {}
                UiState.Idle -> {}
                UiState.Loading -> {
                    if (cachedData == null) {
                        GroupListPlaceholder()
                    } else {
                        cachedData?.let { state ->
                            GroupListSuccess(
                                state = state,
                                onAction = onAction,
                                onNavigate = onNavigate
                            )
                        }
                    }
                }
                is UiState.Success -> GroupListSuccess(
                    state = uiState.data,
                    onAction = onAction,
                    onNavigate = onNavigate
                )
                is UiState.Error -> {}
            }
        }
    }
}

@Composable
fun GroupListSuccess(
    state: GroupListScreenState,
    onAction: (GroupListAction) -> Unit,
    onNavigate: (GroupListNavigation) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.label_title_join_group),
                    style = MaterialTheme.typography.titleLarge,
                    color = Theme.custom.primaryText
                )

                Text(
                    text = stringResource(R.string.label_subtitle_join_group),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Theme.custom.secondaryText
                )
            }
        }

        item {
            Spacer(Modifier.height(8.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = stringResource(R.string.label_invite_code),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Theme.custom.primaryText
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AppTextField(
                        value = state.inviteCode,
                        onValueChange = {
                            onAction(GroupListAction.OnInviteCodeChanged(it))
                        },
                        placeholder = stringResource(R.string.placeholder_invite_code),
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                        modifier = Modifier
                            .weight(0.7f)
                    )

                    AppPrimaryButton(
                        text = stringResource(R.string.btn_label_join),
                        onClick = {
                            onAction(GroupListAction.OnJoinGroupClick)
                        },
                        cornerRadiusPercentage = 30,
                        modifier = Modifier
                            .weight(0.3f)
                            .fillMaxHeight()
                    )
                }
            }
        }

        item {
            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.label_title_inventory_groups),
                style = MaterialTheme.typography.titleLarge,
                color = Theme.custom.primaryText
            )
        }

        items(state.groups, key = { it.groupId }) {group ->
            GroupListItem(
                groupItem = group,
                onClick = {
                    onNavigate(GroupListNavigation.GroupDetail(group.groupId))
                }
            )
        }

        item {
            Spacer(Modifier.height(8.dp))

            AppPrimaryButton(
                onClick = {
                    onNavigate(GroupListNavigation.AddGroup)
                },
                text = stringResource(R.string.btn_label_add_group),
                icon = {
                    Icon(
                        painter = painterResource(coreR.drawable.add_24),
                        contentDescription = null
                    )
                },
                cornerRadiusPercentage = 30,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun GroupListPlaceholder(
    shimmerColor: Color = Theme.custom.shimmer
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Transparent,
                modifier = Modifier
                    .clearAndSetSemantics {}
                    .fillMaxWidth(0.6f)
                    .clip(RoundedCornerShape(50))
                    .shimmerEffect(shimmerColor)
            )

            Text(
                text = "",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Transparent,
                modifier = Modifier
                    .clearAndSetSemantics {}
                    .fillMaxWidth(0.45f)
                    .clip(RoundedCornerShape(50))
                    .shimmerEffect(shimmerColor)
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Color.Transparent,
                modifier = Modifier
                    .clearAndSetSemantics {}
                    .fillMaxWidth(0.3f)
                    .clip(RoundedCornerShape(50))
                    .shimmerEffect(shimmerColor)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .defaultMinSize(minHeight = 60.dp)
                        .weight(0.7f)
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(50))
                        .shimmerEffect(shimmerColor)
                )

                Box(
                    modifier = Modifier
                        .defaultMinSize(minHeight = 50.dp)
                        .weight(0.3f)
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(50))
                        .shimmerEffect(shimmerColor)
                )
            }
        }

        Text(
            text = "",
            style = MaterialTheme.typography.titleLarge,
            color = Color.Transparent,
            modifier = Modifier
                .clearAndSetSemantics {}
                .fillMaxWidth(0.6f)
                .clip(RoundedCornerShape(50))
                .shimmerEffect(shimmerColor)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(5) {
                AppBaseListItemPlaceholder(
                    isTrailingDataLabelExist = false,
                    isTrailingDataExist = true
                )
            }
        }

        Box(
            modifier = Modifier
                .defaultMinSize(minHeight = 50.dp)
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(50))
                .shimmerEffect(shimmerColor)
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun GroupListScreenPreview() {
    InventoryTheme {
        GroupListContent(
            uiState = UiState.Success(
                GroupListScreenState(
                    groups = listOf(
                        GroupListItemState(
                            isActive = true,
                            groupId = "1",
                            name = "Main Warehouse",
                            totalProduct = 128,
                            totalMember = 5
                        ),
                        GroupListItemState(
                            isActive = false,
                            groupId = "2",
                            name = "Main Warehouse",
                            totalProduct = 128,
                            totalMember = 5
                        ),
                        GroupListItemState(
                            isActive = false,
                            groupId = "3",
                            name = "Main Warehouse",
                            totalProduct = 128,
                            totalMember = 5
                        ),
                    ),
                    inviteCode = "",
                )
            ),
            onAction = {},
            onNavigate = {}
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun GroupListScreenPlaceholderPreview() {
    InventoryTheme {
        GroupListContent(
            uiState = UiState.Loading,
            onAction = {},
            onNavigate = {}
        )
    }
}