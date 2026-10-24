package com.kevinfreyap.user.presentation.screen.bottom_sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.domain.model.AppRole
import com.kevinfreyap.domain.util.toTitleCase
import com.kevinfreyap.ui.components.AppOptionSelection
import com.kevinfreyap.ui.components.AppPrimaryButton
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.user.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleAssignmentBottomSheet(
    memberName: String,
    assignableRoles: List<AppRole>,
    onDismissRequest: () -> Unit,
    onApprove: (AppRole) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedRole by rememberSaveable { mutableStateOf<AppRole?>(AppRole.MEMBER) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        contentWindowInsets = { WindowInsets.navigationBars }
    ) {
        RoleAssignmentBottomSheetContent(
            memberName = memberName,
            assignableRoles = assignableRoles,
            selectedRole = selectedRole,
            onRoleSelected = { selectedRole = it },
            onApproveClicked = { selectedRole?.let { onApprove(it) } }
        )
    }
}

@Composable
fun RoleAssignmentBottomSheetContent(
    memberName: String,
    assignableRoles: List<AppRole>,
    selectedRole: AppRole?,
    onRoleSelected: (AppRole) -> Unit,
    onApproveClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.format_label_approve_bottom_sheet, memberName),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Theme.custom.primaryText,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.label_subtitle_assign_role),
            style = MaterialTheme.typography.bodyMedium,
            color = Theme.custom.secondaryText,
        )

        Spacer(modifier = Modifier.height(24.dp))

        AppOptionSelection(
            options = assignableRoles,
            selectedOption = selectedRole,
            onOptionSelected = onRoleSelected,
            titleText = { it.name.toTitleCase() },
            descriptionText = {
                when(it) {
                    AppRole.OWNER -> stringResource(R.string.label_assign_role_description_owner)
                    AppRole.ADMIN -> stringResource(R.string.label_assign_role_description_admin)
                    AppRole.MEMBER -> stringResource(R.string.label_assign_role_description_member)
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        AppPrimaryButton(
            text = "Approve Member",
            onClick = onApproveClicked,
            enabled = selectedRole != null,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview (
    showBackground = true
)
@Composable
fun RoleAssignmentBottomSheetPreview() {
    InventoryTheme {
        RoleAssignmentBottomSheetContent(
            memberName = "John Doe",
            assignableRoles = AppRole.entries,
            selectedRole = AppRole.MEMBER,
            onRoleSelected = {  },
            onApproveClicked = {  }
        )
    }
}