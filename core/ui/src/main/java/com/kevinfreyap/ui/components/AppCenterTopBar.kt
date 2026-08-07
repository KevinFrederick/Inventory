package com.kevinfreyap.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.ui.R
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCenterTopBar (
    title: String,
    onBackClick: () -> Unit,
    isLoading: Boolean? = null,
    actionButton: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title,
                    color = Theme.custom.primaryText,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            actions = { actionButton?.invoke() },
            navigationIcon = {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_left_24),
                        contentDescription = "Back",
                        tint = Theme.custom.primaryText,
                    )
                }
            },
            windowInsets = WindowInsets(top = 0.dp),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        if (isLoading != null && isLoading) {
            LinearProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun AppCenterTopBarPreview() {
    InventoryTheme {
        AppCenterTopBar(
            title = "Add Product",
            onBackClick = {}
        )
    }
}