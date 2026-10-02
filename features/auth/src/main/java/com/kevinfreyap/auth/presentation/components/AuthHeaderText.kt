package com.kevinfreyap.auth.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun AuthHeaderText(
    greeting: String,
    headline: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = greeting,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = headline,
            style = MaterialTheme.typography.headlineMedium,
            color = Theme.custom.primaryText
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = Theme.custom.secondaryText
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun AuthHeaderTextPreview() {
    InventoryTheme {
        AuthHeaderText(
            greeting = "Welcome Back",
            headline = "Access your inventory",
            subtitle = "Manage your inventory, wherever you are",
        )
    }
}