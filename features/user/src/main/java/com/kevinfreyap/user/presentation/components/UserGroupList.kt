package com.kevinfreyap.user.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kevinfreyap.ui.components.AppBaseListItemPlaceholder
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.ui.util.shimmerEffect
import com.kevinfreyap.user.R
import com.kevinfreyap.user.presentation.state.GroupListItemState
import model.MembershipStatus

@Composable
fun UserGroupList(
    totalGroups: Int,
    groups: List<GroupListItemState>,
    onButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    groupItem: @Composable (GroupListItemState) -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.label_inventory_groups))
                    append(" ")
                    withStyle( style = SpanStyle(
                        fontSize = 16.sp
                    )) {
                        append("($totalGroups)")
                    }
                },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium,
                color = Theme.custom.primaryText
            )

            TextButton(
                onClick = onButtonClicked
            ) {
                Text(
                    text = stringResource(R.string.text_btn_manage),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            groups.forEach { group ->
                groupItem(group)
            }
        }
    }
}

@Composable
fun UserGroupListPlaceholder(
    shimmerColor: Color = Theme.custom.shimmer
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium,
                color = Color.Transparent,
                modifier = Modifier
                    .clearAndSetSemantics {}
                    .fillMaxWidth(0.5f)
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(50))
                    .shimmerEffect(shimmerColor)
            )

            Text(
                text = "",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .clearAndSetSemantics {}
                    .fillMaxWidth(0.4f)
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(50))
                    .shimmerEffect(shimmerColor)
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(3) {
                AppBaseListItemPlaceholder(
                    isTrailingDataLabelExist = false,
                    isTrailingDataExist = false
                )
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun UserGroupListPreview() {
    InventoryTheme {
        UserGroupList(
            totalGroups = 6,
            onButtonClicked = {},
            groups = listOf(
                GroupListItemState(
                    isActive = true,
                    groupId = "",
                    name = "Main Warehouse",
                    totalProduct = 128,
                    totalMember = 5,
                    status = MembershipStatus.ACTIVE
                ),
                GroupListItemState(
                    isActive = false,
                    groupId = "",
                    name = "Main Warehouse",
                    totalProduct = 128,
                    totalMember = 5,
                    status = MembershipStatus.ACTIVE
                ),
                GroupListItemState(
                    isActive = false,
                    groupId = "",
                    name = "Main Warehouse",
                    totalProduct = 128,
                    totalMember = 5,
                    status = MembershipStatus.ACTIVE
                ),
            )
        ) { group ->
            GroupListItem(
                groupItem = group,
                onClick = {  },
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun UserGroupListPlaceholderPreview() {
    InventoryTheme {
        UserGroupListPlaceholder()
    }
}