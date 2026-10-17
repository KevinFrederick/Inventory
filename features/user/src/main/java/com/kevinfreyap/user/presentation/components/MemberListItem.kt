package com.kevinfreyap.user.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kevinfreyap.domain.util.toTitleCase
import com.kevinfreyap.ui.components.AppBaseListItem
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.user.presentation.state.MemberListItemState

@Composable
fun MemberListItem(
    memberItem: MemberListItemState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val initials = remember(memberItem.username) {
        val names = memberItem.username.trim().split(" ")
        when {
            names.isEmpty() -> ""
            names.size == 1 -> names[0].take(1).uppercase()
            else -> "${names[0].take(1)}${names.last().take(1)}".uppercase()
        }
    }

    AppBaseListItem(
        itemName = memberItem.username,
        imageUri = memberItem.profilePicture,
        onClick = onClick,
        iconBackground = MaterialTheme.colorScheme.primary.copy(0.3f),
        icon = {
            Text(
                text = initials,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary.copy(
                    red = MaterialTheme.colorScheme.primary.red * 0.8f,
                    green = MaterialTheme.colorScheme.primary.green * 0.8f,
                    blue = MaterialTheme.colorScheme.primary.blue * 0.8f
                )
            )
        },
        subtitle = {
            Text(
                text = memberItem.email,
                style = MaterialTheme.typography.bodyMedium,
                color = Theme.custom.secondaryText
            )
        },
        trailingData = {
            Text(
                text = memberItem.role.toTitleCase(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(end = 8.dp)
            )
        },
        modifier = modifier
    )
}

@Preview(
    showBackground = true
)
@Composable
fun MemberListItemPreview() {
    InventoryTheme {
        MemberListItem(
            onClick = {},
            memberItem = MemberListItemState(
                username = "John",
                email = "JohnDoe@email.com",
                role = "Owner",
                profilePicture = null
            )
        )
    }
}