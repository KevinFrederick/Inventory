package com.kevinfreyap.user.presentation.screen.group_detail.section

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
import com.kevinfreyap.user.presentation.components.MemberListItem
import com.kevinfreyap.user.presentation.state.MemberListItemState

@Composable
fun GroupMembers(
    totalMembers: Int,
    members: List<MemberListItemState>,
    viewMemberClicked: () -> Unit,
    modifier: Modifier = Modifier,
    memberItem: @Composable (MemberListItemState) -> Unit
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
                    append(stringResource(R.string.label_group_members))
                    append(" ")
                    withStyle( style = SpanStyle(
                        fontSize = 16.sp
                    )) {
                        append("($totalMembers)")
                    }
                },
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 20.sp
                ),
                fontWeight = FontWeight.Medium,
                color = Theme.custom.primaryText
            )

            TextButton(
                onClick = viewMemberClicked
            ) {
                Text(
                    text = stringResource(R.string.text_btn_view_all_member),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            members.forEach { member ->
                memberItem(member)
            }
        }
    }
}

@Composable
fun GroupMembersPlaceholder(
    shimmerColor: Color = Theme.custom.shimmer
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 20.sp
                ),
                fontWeight = FontWeight.Medium,
                color = Color.Transparent,
                modifier = Modifier
                    .clearAndSetSemantics {}
                    .fillMaxWidth(0.4f)
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(50))
                    .shimmerEffect(shimmerColor)
            )

            Text(
                text = "",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .clearAndSetSemantics {}
                    .fillMaxWidth(0.5f)
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
                    isTrailingDataExist = true
                )
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun GroupMembersPreview() {
    InventoryTheme {
        GroupMembers(
            totalMembers = 5,
            viewMemberClicked = {},
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
        ) { member ->
            MemberListItem(
                memberItem = member,
                onClick = {}
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun GroupMembersPlaceholderPreview() {
    InventoryTheme {
        GroupMembersPlaceholder()
    }
}