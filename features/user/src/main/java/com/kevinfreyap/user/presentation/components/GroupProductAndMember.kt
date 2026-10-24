package com.kevinfreyap.user.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.user.R

@Composable
fun GroupProductAndMember(
    totalProduct: String,
    totalMember: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        GroupStatCard(
            value = totalProduct,
            label = stringResource(R.string.label_products_in_inventory),
            modifier = Modifier
                .weight(1f)
        )

        GroupStatCard(
            value = totalMember,
            label = stringResource(R.string.label_group_member),
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Composable
fun GroupProductAndMemberPlaceholder() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        GroupStatCardPlaceholder(
            modifier = Modifier
                .weight(1f)
        )
        GroupStatCardPlaceholder(
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun GroupProductAndMemberPreview() {
    InventoryTheme {
        GroupProductAndMember(
            totalProduct = "123",
            totalMember = "6",
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun GroupProductAndMemberPlaceholderPreview() {
    InventoryTheme {
        GroupProductAndMemberPlaceholder()
    }
}