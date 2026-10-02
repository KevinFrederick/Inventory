package com.kevinfreyap.auth.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kevinfreyap.auth.R
import com.kevinfreyap.ui.theme.Theme

@Composable
fun HorizontalDividerOr(
    modifier: Modifier = Modifier
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ){
        HorizontalDivider(
            color = Theme.custom.hint,
            modifier = Modifier
                .weight(1f)
        )

        Text(
            text = stringResource(R.string.label_divider),
            style = MaterialTheme.typography.bodySmall,
            color = Theme.custom.hint,
            modifier = Modifier
                .padding(
                    horizontal = 16.dp
                )
        )

        HorizontalDivider(
            color = Theme.custom.hint,
            modifier = Modifier
                .weight(1f)
        )
    }
}