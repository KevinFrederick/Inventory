package com.kevinfreyap.product.presentation.screen.filter.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.product.R
import com.kevinfreyap.ui.components.AppOutlinedButton
import com.kevinfreyap.ui.components.AppPrimaryButton
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun SectionFilterButtons(
    onNegativeButtonClicked: () -> Unit,
    onPositiveButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        AppOutlinedButton(
            text = stringResource(R.string.btn_label_clear_all),
            onClick = onNegativeButtonClicked,
            borderColor = Theme.custom.primaryText,
            contentColor = Theme.custom.primaryText,
            modifier = Modifier
                .weight(1f)
        )

        AppPrimaryButton(
            text = stringResource(R.string.btn_label_apply_filter),
            onClick = onPositiveButtonClicked,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun SectionFilterButtonsPreview() {
    InventoryTheme { 
        SectionFilterButtons(
            onNegativeButtonClicked = {  },
            onPositiveButtonClicked = {  },
        )
    }
}