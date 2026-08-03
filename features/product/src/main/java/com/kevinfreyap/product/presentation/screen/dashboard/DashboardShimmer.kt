package com.kevinfreyap.product.presentation.screen.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.product.presentation.screen.dashboard.section.SectionCountRowPlaceholder
import com.kevinfreyap.product.presentation.screen.dashboard.section.SectionGreetingsPlaceholder
import com.kevinfreyap.product.presentation.screen.dashboard.section.SectionListWithHeaderPlaceholder
import com.kevinfreyap.ui.theme.InventoryTheme

@Composable
fun DashboardShimmer() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        SectionGreetingsPlaceholder()
        SectionCountRowPlaceholder()
        SectionListWithHeaderPlaceholder()
        SectionListWithHeaderPlaceholder()
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1080px,height=2340px,dpi=416",
    showSystemUi = true
)
@Composable
fun DashboardShimmerPreview() {
    InventoryTheme {
        DashboardShimmer()
    }
}