package com.kevinfreyap.user.presentation.screen.group_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kevinfreyap.domain.model.AppRole
import com.kevinfreyap.ui.components.AppBaseListItem
import com.kevinfreyap.ui.components.AppBaseListItemPlaceholder
import com.kevinfreyap.ui.components.AppCenterTopBar
import com.kevinfreyap.ui.components.AppOutlinedButton
import com.kevinfreyap.ui.components.AppPrimaryButton
import com.kevinfreyap.ui.state.UiState
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.ui.util.shimmerEffect
import com.kevinfreyap.user.R
import com.kevinfreyap.user.presentation.components.GroupActiveLabel
import com.kevinfreyap.ui.R as coreR
import com.kevinfreyap.user.presentation.components.GroupProductAndMember
import com.kevinfreyap.user.presentation.components.GroupProductAndMemberPlaceholder
import com.kevinfreyap.user.presentation.components.MemberListItem
import com.kevinfreyap.user.presentation.navigation.GroupDetailNavigation
import com.kevinfreyap.user.presentation.screen.group_detail.section.GroupDescription
import com.kevinfreyap.user.presentation.screen.group_detail.section.GroupDescriptionPlaceholder
import com.kevinfreyap.user.presentation.screen.group_detail.section.GroupInformation
import com.kevinfreyap.user.presentation.screen.group_detail.section.GroupInformationPlaceholder
import com.kevinfreyap.user.presentation.screen.group_detail.section.GroupMembers
import com.kevinfreyap.user.presentation.screen.group_detail.section.GroupMembersPlaceholder
import com.kevinfreyap.user.presentation.state.GroupDetailScreenState
import com.kevinfreyap.user.presentation.state.GroupDetailState
import com.kevinfreyap.user.presentation.state.MemberListItemState

@Composable
fun GroupDetailScreen(
    onNavigate: (GroupDetailNavigation) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GroupDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GroupDetailContent(
        state = uiState,
        onNavigate = onNavigate,
        modifier = modifier
    )
}

@Composable
fun GroupDetailContent(
    state: UiState<GroupDetailScreenState>,
    onNavigate: (GroupDetailNavigation) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            AppCenterTopBar(
                title = "",
                onBackClick = {
                    onNavigate(GroupDetailNavigation.NavigateUp)
                },
                actionButton = {
                    if (state is UiState.Success && state.data.currentUserRole == AppRole.OWNER) {
                        IconButton(
                            onClick = {

                            }
                        ) {
                            Icon(
                                painter = painterResource(coreR.drawable.delete_24),
                                contentDescription = stringResource(R.string.btn_label_logout),
                                tint = Theme.custom.primaryText
                            )
                        }
                    }
                },
            )
        }
    ) { innerPadding->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .padding(innerPadding)
                .padding(
                    top = 4.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            when(state) {
                UiState.Empty -> {}
                UiState.Idle -> {}
                UiState.Loading -> GroupDetailLoading()
                is UiState.Success -> GroupDetailSuccess(
                    state = state.data
                )
                is UiState.Error -> {}
            }
        }
    }
}

@Composable
fun GroupDetailSuccess(
    state: GroupDetailScreenState
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = state.groupDetail.name,
            style = MaterialTheme.typography.titleLarge,
            color = Theme.custom.primaryText
        )

        if (state.isActive) {
            GroupActiveLabel(
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(end = 16.dp)
            )
        }
    }

    if (!state.groupDetail.description.isNullOrBlank()) {
        GroupDescription(
            description = state.groupDetail.description
        )
    }

    GroupProductAndMember(
        totalProduct = state.groupDetail.totalProducts.toString(),
        totalMember = state.groupDetail.totalMembers.toString(),
    )

    AppOutlinedButton(
        text = stringResource(R.string.btn_label_edit_group),
        cornerRadiusPercentage = 30,
        icon = {
            Icon(
                painter = painterResource(coreR.drawable.edit_24),
                contentDescription = null
            )
        },
        borderColor = if (state.currentUserRole == AppRole.OWNER) Theme.custom.primaryText else Theme.custom.hint,
        onClick = {},
        enabled = state.currentUserRole == AppRole.OWNER,
        modifier = Modifier
            .fillMaxWidth()
    )

    GroupInformation(
        groupAddress = state.groupDetail.address,
        groupOwner = state.groupDetail.owner,
        groupCreated = state.groupDetail.created,
    )

    AppBaseListItem(
        itemName = stringResource(R.string.btn_label_browse_inventory),
        subtitle = {
            Text(
                text = if (state.isActive) stringResource(R.string.btn_label_view_products) else stringResource(R.string.btn_label_set_active),
                style = MaterialTheme.typography.bodySmall,
                color = Theme.custom.secondaryText
            )
        },
        onClick = {},
        iconBackground = MaterialTheme.colorScheme.primary.copy(0.3f),
        icon = {
            Icon(
                painter = painterResource(coreR.drawable.app_boxes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(32.dp)
            )
        },
        trailingData = {
            Icon(
                painter = painterResource(coreR.drawable.arrow_right_24),
                contentDescription = null,
                tint = Theme.custom.hint
            )
        },
    )

    GroupMembers(
        totalMembers = state.groupDetail.totalMembers,
        viewMemberClicked = {},
        members = state.members
    ) { member ->
        MemberListItem(
            memberItem = member,
            onClick = {}
        )
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = if (state.currentUserRole == AppRole.OWNER) {
                painterResource(R.drawable.app_shield_check)
            } else {
                painterResource(R.drawable.shield_24)
            },
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = if (state.currentUserRole == AppRole.OWNER) {
                    stringResource(R.string.label_title_has_access)
                } else {
                    stringResource(R.string.label_title_no_access)
                },
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = Theme.custom.primaryText
            )

            Text(
                text = if (state.currentUserRole == AppRole.OWNER){
                    stringResource(R.string.label_subtitle_has_access)
                } else {
                    stringResource(R.string.label_subtitle_no_access)
                },
                style = MaterialTheme.typography.bodySmall,
                color = Theme.custom.secondaryText
            )
        }
    }

    if (state.currentUserRole == AppRole.OWNER) {
        AppPrimaryButton(
            text = stringResource(R.string.btn_label_invite_member),
            onClick = {},
            cornerRadiusPercentage = 30,
            icon = {
                Icon(
                    painter = painterResource(coreR.drawable.add_24),
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun GroupDetailLoading(
    shimmerColor: Color = Theme.custom.shimmer
) {
    Text(
        text = "",
        style = MaterialTheme.typography.titleLarge,
        color = Theme.custom.primaryText,
        modifier = Modifier
            .clearAndSetSemantics {}
            .fillMaxWidth(0.7f)
            .clip(RoundedCornerShape(50))
            .shimmerEffect(shimmerColor)
    )

    GroupDescriptionPlaceholder()

    GroupProductAndMemberPlaceholder()

    Box(
        modifier = Modifier
            .defaultMinSize(minHeight = 50.dp)
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(50))
            .shimmerEffect(shimmerColor)
    )

    GroupInformationPlaceholder()

    AppBaseListItemPlaceholder(
        isTrailingDataExist = false,
        isTrailingDataLabelExist = false
    )

    GroupMembersPlaceholder()

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .shimmerEffect(shimmerColor)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = Color.Transparent,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .clip(RoundedCornerShape(50))
                    .shimmerEffect(shimmerColor)
            )

            Text(
                text = "",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Transparent,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(50))
                    .shimmerEffect(shimmerColor)
            )
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1080px,height=2500px,dpi=416",
)
@Composable
fun GroupDetailScreenPreview() {
    InventoryTheme {
        GroupDetailContent(
            onNavigate = {},
            state = UiState.Success(GroupDetailScreenState(
                isActive = true,
                currentUserRole = AppRole.OWNER,
                groupDetail = GroupDetailState(
                    name = "User's Inventory",
                    description = null,
                    totalProducts = 123,
                    totalMembers = 5,
                    address = null,
                    owner = "John Doe",
                    created = "31 September 2026"
                ),
                members = listOf(
                    MemberListItemState(
                        username = "John Doe",
                        email = "JohnDoe@email.com",
                        role = "Owner",
                        profilePicture = null
                    ),
                    MemberListItemState(
                        username = "Bob",
                        email = "Bob@email.com",
                        role = "Admin",
                        profilePicture = null
                    ),
                    MemberListItemState(
                        username = "Cat",
                        email = "Cat@email.com",
                        role = "Member",
                        profilePicture = null
                    )
                )
            ))
        )
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1080px,height=2500px,dpi=416",
)
@Composable
fun GroupDetailScreenPlaceholderPreview() {
    InventoryTheme {
        GroupDetailContent(
            onNavigate = {},
            state = UiState.Loading
        )
    }
}