package com.kevinfreyap.auth.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.auth.R
import com.kevinfreyap.ui.R as coreR
import com.kevinfreyap.ui.components.AppTextField
import com.kevinfreyap.ui.theme.InventoryTheme

@Composable
fun EmailPasswordInput(
    lastFieldImeAction: ImeAction,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(coreR.string.label_email),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            AppTextField(
                value = "",
                onValueChange = {  },
                placeholder = stringResource(R.string.placeholder_email),
                imeAction = ImeAction.Next
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(coreR.string.label_password),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            AppTextField(
                value = "",
                onValueChange = {  },
                placeholder = stringResource(R.string.placeholder_password),
                imeAction = lastFieldImeAction,
                keyboardType = KeyboardType.Password
            )
        }


    }
}

@Preview(
    showBackground = true
)
@Composable
fun EmailPasswordInputPreview() {
    InventoryTheme {
        EmailPasswordInput(
            lastFieldImeAction = ImeAction.Done
        )
    }
}