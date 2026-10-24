package com.kevinfreyap.user.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.ui.components.AppOutlinedButton
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.ui.util.shimmerEffect
import com.kevinfreyap.user.R
import com.kevinfreyap.ui.R as coreR
import com.kevinfreyap.user.presentation.state.UserDetailState

@Composable
fun UserProfileDetail(
    user: UserDetailState,
    onButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            ProfileImage(
                imageUrl = user.profileUrl,
                size = 72.dp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Medium,
                    color = Theme.custom.primaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Theme.custom.secondaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }

        AppOutlinedButton(
            onClick = onButtonClicked,
            icon = {
                Icon(
                    painter = painterResource(coreR.drawable.edit_24),
                    contentDescription = null
                )
            },
            text = stringResource(R.string.btn_label_edit_profile),
            cornerRadiusPercentage = 30,
            modifier = Modifier
                .fillMaxWidth()
        )

        ProfileInformation(
            userPhoneNumber = user.phoneNumber,
            userJobTitle = user.jobTitle,
        )
    }
}

@Composable
fun UserProfileDetailPlaceholder(
    shimmerColor: Color = Theme.custom.shimmer
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ProfileImagePlaceholder(
                shimmerColor = shimmerColor,
                size = 72.dp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(24.dp)
                        .clip(RoundedCornerShape(50))
                        .shimmerEffect(shimmerColor)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(16.dp)
                        .clip(RoundedCornerShape(50))
                        .shimmerEffect(shimmerColor)
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

        ProfileInformationPlaceholder()
    }
}

@Preview(
    showBackground = true
)
@Composable
fun UserProfileDetailPreview() {
    InventoryTheme {
        UserProfileDetail(
            user = UserDetailState(
                name = "Username 1",
                email = "user@email.com",
                profileUrl = null,
                phoneNumber = "+62 8123 4567 890",
                jobTitle = "Operation Manager"
            ),
            onButtonClicked = {}
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun UserProfileDetailPlaceholderPreview() {
    InventoryTheme {
        UserProfileDetailPlaceholder()
    }
}