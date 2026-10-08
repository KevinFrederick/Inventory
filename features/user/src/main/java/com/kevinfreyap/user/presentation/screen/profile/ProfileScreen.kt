package com.kevinfreyap.user.presentation.screen.profile

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kevinfreyap.ui.components.AppIconName
import com.kevinfreyap.ui.components.AppPrimaryButton
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileContent(
        uiState = uiState,
        onNavigate = onNavigate,
        modifier = modifier
    )
}

@Composable
fun ProfileContent(
    uiState: UiState<ProfileScreenState>,
    onNavigate: (ProfileNavigation) -> Unit,
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
                            groupId = "",
                            name = "Main Warehouse",
                            totalProduct = 128,
                            totalMember = 5
                        ),
                        GroupListItemState(
                            groupId = "",
                            name = "Main Warehouse",
                            totalProduct = 128,
                            totalMember = 5
                        ),
                        GroupListItemState(
                            groupId = "",
                            name = "Main Warehouse",
                            totalProduct = 128,
                            totalMember = 5
                        ),
                    ),
                )
            ),
            onNavigate = {}
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
            onNavigate = {}
        )
    }
}