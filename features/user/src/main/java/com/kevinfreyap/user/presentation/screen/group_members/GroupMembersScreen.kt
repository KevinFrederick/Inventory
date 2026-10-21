package com.kevinfreyap.user.presentation.screen.group_members

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kevinfreyap.domain.model.AppRole
import com.kevinfreyap.ui.components.AppBaseListItemPlaceholder
import com.kevinfreyap.ui.components.AppCenterTopBar
import com.kevinfreyap.ui.components.AppPrimaryButton
import com.kevinfreyap.ui.state.UiState
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.ui.util.shimmerEffect
import com.kevinfreyap.user.R
import com.kevinfreyap.user.presentation.action.MembersAction
import com.kevinfreyap.user.presentation.components.MemberListItem
import com.kevinfreyap.user.presentation.components.PendingMemberListItem
import com.kevinfreyap.user.presentation.components.PendingMemberListItemPlaceholder
import com.kevinfreyap.user.presentation.navigation.GroupMemberNavigation
import com.kevinfreyap.user.presentation.screen.bottom_sheet.RoleAssignmentBottomSheet
import com.kevinfreyap.user.presentation.screen.bottom_sheet.ShareCodeBottomSheet
import com.kevinfreyap.user.presentation.state.GroupMembersScreenState
import com.kevinfreyap.user.presentation.state.MemberListItemState
import com.kevinfreyap.user.presentation.state.PendingMemberListItemState

@Composable
fun GroupMembersScreen(
    modifier: Modifier = Modifier,
    onNavigate: (GroupMemberNavigation) -> Unit,
    viewModel: GroupMembersViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var selectedPendingMemberId by rememberSaveable { mutableStateOf<String?>(null) }

    val shareCodeText = stringResource(R.string.label_share_code, "%s")
    val shareChooserTitle = stringResource(R.string.btn_label_share_invite_code)

    GroupMembersContent(
        uiState = uiState,
        selectedPendingMemberId = selectedPendingMemberId,
        onAction = { action ->
            when(action) {
                is MembersAction.OnShareInviteCodeClicked -> {
                    val sendIntent = Intent().apply {
                        this.action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareCodeText.format(action.code))
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, shareChooserTitle))
                }
                else -> {
                    viewModel.onAction(action)
                }
            }
        },
        onNavigate = onNavigate,
        onSelectPendingMember = { memberId ->
            selectedPendingMemberId = memberId
        },
        modifier = modifier
    )
}

@Composable
fun GroupMembersContent(
    uiState: UiState<GroupMembersScreenState>,
    selectedPendingMemberId: String?,
    onAction: (MembersAction) -> Unit,
    onNavigate: (GroupMemberNavigation) -> Unit,
    onSelectPendingMember: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var cachedData by remember { mutableStateOf<GroupMembersScreenState?>(null) }

    if (uiState is UiState.Success) {
        cachedData = uiState.data
    }

    Scaffold(
        topBar = {
            AppCenterTopBar(
                title = "",
                onBackClick = {
                    onNavigate(GroupMemberNavigation.NavigateUp)
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
                UiState.Idle -> {}
                UiState.Empty -> {}
                UiState.Loading -> {
                    if (cachedData == null) {
                        GroupMemberPlaceholder()
                    } else {
                        cachedData?.let { state ->
                            GroupMembersSuccess(
                                state = state,
                                onAction = onAction,
                                onSelectPendingMember = onSelectPendingMember
                            )
                        }
                    }
                }
                is UiState.Success -> {
                    val state = uiState.data

                    val memberToApprove = remember(state.pendingMembers, selectedPendingMemberId) {
                        state.pendingMembers.find { it.memberId == selectedPendingMemberId }
                    }

                    GroupMembersSuccess(
                        state = uiState.data,
                        onAction = onAction,
                        onSelectPendingMember = onSelectPendingMember
                    )

                    memberToApprove?.let { member ->
                        RoleAssignmentBottomSheet(
                            memberName = member.requestName,
                            assignableRoles = state.assignableRoles,
                            onDismissRequest = {
                                onSelectPendingMember(null)
                            },
                            onApprove = { role ->
                                onAction(
                                    MembersAction.OnApproveMember(
                                        memberId = member.memberId,
                                        role = role
                                    )
                                )
                                onSelectPendingMember(null)
                            }
                        )
                    }

                    state.generatedInviteCode?.let { code ->
                        ShareCodeBottomSheet(
                            inviteCode = code,
                            onDismissRequest = { onAction(MembersAction.OnDismissShareSheet) },
                            onShareClick = {
                                onAction(MembersAction.OnShareInviteCodeClicked(code))
                                onAction(MembersAction.OnDismissShareSheet)
                            }
                        )
                    }
                }
                is UiState.Error -> {}
            }
        }
    }
}

@Composable
fun GroupMembersSuccess(
    state: GroupMembersScreenState,
    onAction: (MembersAction) -> Unit,
    onSelectPendingMember: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val isAdminOrOwner = state.loggedInUserRole == AppRole.OWNER || state.loggedInUserRole == AppRole.ADMIN

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        if (isAdminOrOwner && state.pendingMembers.isNotEmpty()) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.label_title_pending_member),
                        style = MaterialTheme.typography.titleLarge,
                        color = Theme.custom.primaryText
                    )

                    Text(
                        text = stringResource(R.string.label_subtitle_pending_members, state.pendingMembers.size),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Theme.custom.secondaryText
                    )
                }
            }

            items(state.pendingMembers, key = { it.memberId }) { pendingMembers ->
                PendingMemberListItem(
                    requestName = pendingMembers.requestName,
                    requestEmail = pendingMembers.requestEmail,
                    requestDate = pendingMembers.requestDate,
                    onReject = {
                        onAction(MembersAction.OnRejectMember(pendingMembers.memberId))
                    },
                    onApprove = {
                        onSelectPendingMember(pendingMembers.memberId)
                    }
                )
            }
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.label_title_member),
                    style = MaterialTheme.typography.titleLarge,
                    color = Theme.custom.primaryText
                )

                Text(
                    text = stringResource(R.string.label_subtitle_members, state.activeMembers.size),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Theme.custom.secondaryText
                )
            }
        }

        items(state.activeMembers, key = { it.memberId }) {activeMembers ->
            MemberListItem(
                memberItem = activeMembers,
                onClick = { }
            )
        }

        if (isAdminOrOwner) {
            item {
                Spacer(Modifier.heightIn(8.dp))

                AppPrimaryButton(
                    text = "Generate Invite Code",
                    onClick = {
                        onAction(MembersAction.OnGenerateInviteCode)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.share_24),
                            contentDescription = null,
                        )
                    },
                    cornerRadiusPercentage = 30,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun GroupMemberPlaceholder(
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) {
                PendingMemberListItemPlaceholder()
            }
        }

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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(2) {
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
fun GroupMembersScreenPreview() {
    InventoryTheme {
        GroupMembersContent(
            uiState = UiState.Success(
                GroupMembersScreenState(
                    activeMembers = listOf(
                        MemberListItemState(
                            memberId = "1",
                            username = "John",
                            email = "JohnDoe@email.com",
                            role = "Owner",
                            profilePicture = null
                        ),
                        MemberListItemState(
                            memberId = "2",
                            username = "John",
                            email = "JohnDoe@email.com",
                            role = "Admin",
                            profilePicture = null
                        )
                    ),
                    pendingMembers = listOf(
                        PendingMemberListItemState(
                            memberId = "req1",
                            requestName = "John doe",
                            requestEmail = "JohnDoe@email.com",
                            requestDate = "19 October 2026"
                        ),
                        PendingMemberListItemState(
                            memberId = "req2",
                            requestName = "John doe",
                            requestEmail = "JohnDoe@email.com",
                            requestDate = "19 October 2026"
                        ),
                        PendingMemberListItemState(
                            memberId = "req3",
                            requestName = "John doe",
                            requestEmail = "JohnDoe@email.com",
                            requestDate = "19 October 2026"
                        ),
                    ),
                    loggedInUserRole = AppRole.OWNER,
                )
            ),
            selectedPendingMemberId = null,
            onAction = {},
            onSelectPendingMember = {},
            onNavigate = {}
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun GroupMemberScreenPlaceholderPreview() {
    InventoryTheme {
        GroupMembersContent(
            uiState = UiState.Loading,
            selectedPendingMemberId = null,
            onAction = {},
            onSelectPendingMember = {},
            onNavigate = {}
        )
    }
}