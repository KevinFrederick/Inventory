package com.kevinfreyap.user.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.ui.components.AppOutlinedButton
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.ui.util.shimmerEffect
import com.kevinfreyap.user.R

@Composable
fun PendingMemberListItem(
    requestName: String,
    requestEmail: String,
    requestDate: String,
    onReject: () -> Unit,
    onApprove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(0.3f)
                        )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.mail_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Text(
                        text = requestName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = Theme.custom.primaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = requestEmail,
                        style = MaterialTheme.typography.bodySmall,
                        color = Theme.custom.secondaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = stringResource(R.string.format_label_request_date, requestDate),
                        style = MaterialTheme.typography.bodySmall,
                        color = Theme.custom.secondaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AppOutlinedButton(
                    text = stringResource(R.string.btn_label_reject_member),
                    onClick = onReject,
                    cornerRadiusPercentage = 30,
                    borderColor = Theme.custom.hint,
                    contentColor = Theme.custom.secondaryText,
                    modifier = Modifier
                        .weight(1f)
                )

                AppOutlinedButton(
                    text = stringResource(R.string.btn_label_approve_member),
                    onClick = onApprove,
                    cornerRadiusPercentage = 30,
                    borderColor = MaterialTheme.colorScheme.primary.copy(0.7f),
                    contentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
fun PendingMemberListItemPlaceholder(
    shimmerColor: Color = Theme.custom.shimmer
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .shimmerEffect(shimmerColor)
                ) {}

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Text(
                        text = "",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = Color.Transparent,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .clearAndSetSemantics {}
                            .fillMaxWidth(0.3f)
                            .clip(RoundedCornerShape(50))
                            .shimmerEffect(shimmerColor)
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Transparent,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .clearAndSetSemantics {}
                            .fillMaxWidth(0.5f)
                            .clip(RoundedCornerShape(50))
                            .shimmerEffect(shimmerColor)
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Transparent,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .clearAndSetSemantics {}
                            .fillMaxWidth(0.6f)
                            .clip(RoundedCornerShape(50))
                            .shimmerEffect(shimmerColor)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(30))
                        .shimmerEffect(shimmerColor)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(30))
                        .shimmerEffect(shimmerColor)
                )
            }
        }
    }
}

@Preview(
    showBackground = false
)
@Composable
fun PendingMemberListItemPreview() {
    InventoryTheme {
        PendingMemberListItem(
            requestName = "John doe",
            requestEmail = "JohnDoe@email.com",
            requestDate = "19 October 2026",
            onReject = {},
            onApprove = {}
        )
    }
}

@Preview
@Composable
fun PendingMemberListItemPlaceholderPreview() {
    InventoryTheme {
        PendingMemberListItemPlaceholder()
    }
}