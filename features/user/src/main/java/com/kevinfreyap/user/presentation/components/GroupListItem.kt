package com.kevinfreyap.user.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.ui.components.AppBaseListItem
import com.kevinfreyap.ui.components.AppBaseListItemPlaceholder
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.user.R
import com.kevinfreyap.user.presentation.state.GroupListItemState
import model.MembershipStatus
import com.kevinfreyap.ui.R as coreR

@Composable
fun GroupListItem(
    groupItem: GroupListItemState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    AppBaseListItem(
        itemName = groupItem.name,
        onClick = onClick,
        enabled = enabled,
        iconBackground = MaterialTheme.colorScheme.primary.copy(0.3f),
        icon = {
            Icon(
                painter = painterResource(coreR.drawable.app_box_icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(32.dp)
            )
        },
        subtitle = {
            Text(
                text = stringResource(
                    R.string.format_label_group_list,
                    groupItem.totalProduct,
                    groupItem.totalMember
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = Theme.custom.secondaryText
            )
        },
        trailingData = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (groupItem.isActive) {
                    GroupActiveLabel()
                }

                if (enabled) {
                    Icon(
                        painter = painterResource(coreR.drawable.arrow_right_24),
                        contentDescription = null,
                        tint = Theme.custom.hint
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun GroupListItemPreview() {
    InventoryTheme {
        GroupListItem(
            groupItem = GroupListItemState(
                isActive = true,
                groupId = "",
                name = "Main Warehouse",
                totalProduct = 128,
                totalMember = 5,
                status = MembershipStatus.ACTIVE
            ),
            onClick = {}
        )
    }
}

@Preview
@Composable
fun GroupListItemPlaceholderPreview() {
    InventoryTheme {
        AppBaseListItemPlaceholder(
            isTrailingDataLabelExist = false,
            isTrailingDataExist = false
        )
    }
}