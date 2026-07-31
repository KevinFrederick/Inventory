package com.kevinfreyap.product.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.product.R
import com.kevinfreyap.product.domain.model.query.sort.SortDirection
import com.kevinfreyap.ui.theme.InventoryTheme

@Composable
fun SortDirectionButton(
    sortDirection: SortDirection,
    @StringRes sortLabelRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(50),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 12.dp)
        ) {
            Icon(
                painter = when (sortDirection) {
                    SortDirection.ASCENDING -> painterResource(R.drawable.arrow_upward_24)
                    SortDirection.DESCENDING -> painterResource(R.drawable.arrow_downward_24)
                },
                contentDescription = "Direction Icon",
                tint = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(
                modifier = Modifier.width(4.dp)
            )

            Text(
                text = stringResource(sortLabelRes),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(end = 4.dp)
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SortDirectionButtonPreview() {
    InventoryTheme {
        SortDirectionButton(
            sortDirection = SortDirection.DESCENDING,
            sortLabelRes = R.string.sort_direction_desc_date,
            onClick = {},
        )
    }
}