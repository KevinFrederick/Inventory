package com.kevinfreyap.user.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.ui.util.shimmerEffect
import com.kevinfreyap.user.R

@Composable
fun ProfileInformation(
    userPhoneNumber: String?,
    userJobTitle: String?,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = Theme.custom.hint
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 12.dp,
                        start = 12.dp,
                        end = 12.dp
                    )
            ) {
                Text(
                    text = stringResource(R.string.label_phone_number),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Theme.custom.secondaryText
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = if (!userPhoneNumber.isNullOrBlank()) userPhoneNumber else stringResource(R.string.default_phone_number),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (!userPhoneNumber.isNullOrBlank()) Theme.custom.primaryText else Theme.custom.hint,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f, fill = false)
                )
            }

            HorizontalDivider(
                color = Theme.custom.shimmer,
                modifier = Modifier
                    .padding(vertical = 12.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 12.dp,
                        start = 12.dp,
                        end = 12.dp
                    )
            ) {
                Text(
                    text = stringResource(R.string.label_job_title),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Theme.custom.secondaryText
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = if (!userJobTitle.isNullOrBlank()) userJobTitle else stringResource(R.string.default_job_title),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (!userJobTitle.isNullOrBlank()) Theme.custom.primaryText else Theme.custom.hint,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f, fill = false)
                )
            }
        }
    }
}

@Composable
fun ProfileInformationPlaceholder(
    shimmerColor: Color = Theme.custom.shimmer
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = Theme.custom.hint
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 12.dp,
                        start = 12.dp,
                        end = 12.dp
                    )
            ) {
                Text(
                    text = "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Transparent,
                    modifier = Modifier
                        .clearAndSetSemantics {}
                        .fillMaxWidth(0.3f)
                        .clip(RoundedCornerShape(50))
                        .shimmerEffect(shimmerColor)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Transparent,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .fillMaxWidth(0.7f)
                        .clip(RoundedCornerShape(50))
                        .shimmerEffect(shimmerColor)
                )
            }

            HorizontalDivider(
                color = Theme.custom.shimmer,
                modifier = Modifier
                    .padding(vertical = 12.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 12.dp,
                        start = 12.dp,
                        end = 12.dp
                    )
            ) {
                Text(
                    text = "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Transparent,
                    modifier = Modifier
                        .clearAndSetSemantics {}
                        .fillMaxWidth(0.2f)
                        .clip(RoundedCornerShape(50))
                        .shimmerEffect(shimmerColor)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Transparent,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .fillMaxWidth(0.5f)
                        .clip(RoundedCornerShape(50))
                        .shimmerEffect(shimmerColor)
                )
            }
        }
    }
}

@Preview
@Composable
fun ProfileInformationPreview() {
    InventoryTheme {
        ProfileInformation(
            userPhoneNumber = "+62 8123 4567 890",
            userJobTitle = ""
        )
    }
}

@Preview
@Composable
fun ProfileInformationPlaceholderPreview() {
    InventoryTheme {
        ProfileInformationPlaceholder()
    }
}