package com.kevinfreyap.user.presentation.screen.bottom_sheet

import android.content.ClipData
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.ui.components.AppOutlinedButton
import com.kevinfreyap.ui.components.AppPrimaryButton
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.user.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareCodeBottomSheet(
    inviteCode: String,
    onDismissRequest: () -> Unit,
    onShareClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()

    val clipDataLabel = stringResource(R.string.label_group_code)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        contentWindowInsets = { WindowInsets.navigationBars }
    ) {
        ShareCodeBottomSheetContent(
            inviteCode = inviteCode,
            onShareClick = onShareClick,
            onCopyClick = {
                scope.launch {
                    val clipData = ClipData.newPlainText(clipDataLabel, inviteCode)
                    clipboard.setClipEntry(ClipEntry(clipData))
                    onDismissRequest()
                }
            }
        )
    }
}

@Composable
fun ShareCodeBottomSheetContent(
    inviteCode: String,
    onShareClick: () -> Unit,
    onCopyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp, top = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.label_group_code),
            style = MaterialTheme.typography.titleMedium,
            color = Theme.custom.primaryText
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = inviteCode,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = stringResource(R.string.label_valid_days, 7),
                style = MaterialTheme.typography.bodyLarge,
                color = Theme.custom.secondaryText
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            AppOutlinedButton(
                text = stringResource(R.string.btn_label_copy_code),
                onClick = onCopyClick,
                modifier = Modifier.weight(1f)
            )

            AppPrimaryButton(
                text = stringResource(R.string.btn_label_share_code),
                onClick = onShareClick,
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.share_24),
                        contentDescription = null,
                    )
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShareCodeBottomSheetContentPreview() {
    InventoryTheme {
        ShareCodeBottomSheetContent(
            inviteCode = "GROUP-A7B9X",
            onShareClick = {},
            onCopyClick = {}
        )
    }
}