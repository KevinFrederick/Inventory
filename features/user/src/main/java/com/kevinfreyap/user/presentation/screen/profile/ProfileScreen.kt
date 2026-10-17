package com.kevinfreyap.user.presentation.screen.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.kevinfreyap.ui.components.AppIconName
import com.kevinfreyap.ui.components.AppOutlinedButton
import com.kevinfreyap.ui.components.AppPrimaryButton
import com.kevinfreyap.ui.components.AppTextIconDialog
import com.kevinfreyap.ui.event.UiEvent
import com.kevinfreyap.ui.state.UiState
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.ui.util.shimmerEffect
import com.kevinfreyap.user.R
import com.kevinfreyap.user.presentation.components.GroupListItem
import com.kevinfreyap.user.presentation.components.UserGroupList
import com.kevinfreyap.user.presentation.components.UserGroupListPlaceholder
import com.kevinfreyap.user.presentation.components.UserProfileDetail
import com.kevinfreyap.user.presentation.components.UserProfileDetailPlaceholder
import com.kevinfreyap.user.presentation.navigation.ProfileNavigation
import com.kevinfreyap.user.presentation.state.GroupListItemState
import com.kevinfreyap.user.presentation.state.ProfileScreenState
import com.kevinfreyap.user.presentation.state.UserDetailState

@Composable
fun ProfileScreen(
    onNavigate: (ProfileNavigation) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.uiEvent, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEvent.collect { event ->
                when(event) {
                    is UiEvent.Navigate -> {
                        onNavigate(event.destination)
                    }
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

    ProfileContent(
        uiState = uiState,
        onNavigate = onNavigate,
        onLogout = { viewModel.logout() },
        modifier = modifier
    )
}

@Composable
fun ProfileContent(
    uiState: UiState<ProfileScreenState>,
    onNavigate: (ProfileNavigation) -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLogoutDialog by rememberSaveable { mutableStateOf(false) }

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
                    onClick = {
                        showLogoutDialog = true
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.logout_24),
                        contentDescription = stringResource(R.string.btn_label_logout),
                        tint = Theme.custom.primaryText
                    )
                }
            }
        },
    ) {innerPadding ->
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
            when(uiState) {
                is UiState.Idle -> {}
                is UiState.Loading -> ProfileShimmer()
                is UiState.Empty -> {}
                is UiState.Error -> {}
                is UiState.Success -> ProfileSuccess(uiState.data, onNavigate)
            }
        }
    }

    if (showLogoutDialog) {
        AppTextIconDialog(
            icon = painterResource(R.drawable.logout_24),
            title = stringResource(R.string.dialog_label_title_logout),
            subtitle = stringResource(R.string.dialog_label_subtitle_logout),
            iconColor = MaterialTheme.colorScheme.primary,
            onDismissRequest = {
                showLogoutDialog = false
            },
            positiveBtn = {
                AppPrimaryButton(
                    text = stringResource(R.string.btn_label_confirm),
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            negativeBtn = {
                AppOutlinedButton(
                    text = stringResource(R.string.btn_label_cancel),
                    onClick = {
                        showLogoutDialog = false
                    },
                    borderColor = Theme.custom.hint,
                    contentColor = Theme.custom.hint,
                    modifier = Modifier.fillMaxWidth()
                )
            },
        )
    }
}

@Composable
fun ProfileSuccess(
    profileState: ProfileScreenState,
    onNavigate: (ProfileNavigation) -> Unit
) {
    Text(
        text = stringResource(R.string.title_account_profile),
        style = MaterialTheme.typography.titleLarge,
        color = Theme.custom.primaryText
    )

    UserProfileDetail(
        user = profileState.userDetail,
        onButtonClicked = {
            onNavigate(ProfileNavigation.EditProfile)
        }
    )

    UserGroupList(
        totalGroups = profileState.totalGroups,
        onButtonClicked = {
            onNavigate(ProfileNavigation.ManageGroups)
        },
        groups = profileState.groups
    ) { group ->
        GroupListItem(
            groupItem = group,
            onClick = {
                onNavigate(ProfileNavigation.GroupDetail(groupId = group.groupId))
            },
        )
    }

    AppPrimaryButton(
        onClick = {
            onNavigate(ProfileNavigation.AddGroup)
        },
        text = stringResource(R.string.btn_label_add_group),
        cornerRadiusPercentage = 30,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun ProfileShimmer(
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
    UserProfileDetailPlaceholder()

    UserGroupListPlaceholder()

    Box(
        modifier = Modifier
            .defaultMinSize(minHeight = 50.dp)
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(50))
            .shimmerEffect(shimmerColor)
    )
}

@Preview(
    showBackground = true
)
@Composable
fun ProfileScreenPreview() {
    InventoryTheme {
        ProfileContent(
            uiState = UiState.Success(
                ProfileScreenState(
                    userDetail = UserDetailState(
                        name = "Username 1",
                        email = "user@email.com",
                        profileUrl = null,
                        phoneNumber = "+62 8123 4567 890",
                        jobTitle = "Operation Manager"
                    ),
                    totalGroups = 5,
                    groups = listOf(
                        GroupListItemState(
                            isActive = true,
                            groupId = "",
                            name = "Main Warehouse",
                            totalProduct = 128,
                            totalMember = 5
                        ),
                        GroupListItemState(
                            isActive = false,
                            groupId = "",
                            name = "Main Warehouse",
                            totalProduct = 128,
                            totalMember = 5
                        ),
                        GroupListItemState(
                            isActive = false,
                            groupId = "",
                            name = "Main Warehouse",
                            totalProduct = 128,
                            totalMember = 5
                        ),
                    ),
                )
            ),
            onNavigate = {},
            onLogout = {}
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun ProfileScreenPlaceholderPreview() {
    InventoryTheme {
        ProfileContent(
            uiState = UiState.Loading,
            onNavigate = {},
            onLogout = {}
        )
    }
}