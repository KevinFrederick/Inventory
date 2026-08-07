package com.kevinfreyap.product.presentation.screen.bottom_sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.product.R
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerBottomSheet(
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        ImagePickerBottomSheetContent(
            onDismiss = onDismiss,
            onCameraClick = onCameraClick,
            onGalleryClick = onGalleryClick,
            modifier = modifier
        )
    }
}

@Composable
fun ImagePickerBottomSheetContent(
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
    ) {
        Text(
            text = stringResource(R.string.title_image_picker),
            style = MaterialTheme.typography.titleMedium,
            color = Theme.custom.primaryText,
        )

        Spacer(Modifier.height(24.dp))

        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.label_image_picker_camera),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Theme.custom.primaryText
                )
            },
            leadingContent = {
                Icon(
                    painter = painterResource(R.drawable.photo_camera_24),
                    contentDescription = null,
                    tint = Theme.custom.primaryText
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier
                .clickable {
                    onCameraClick()
                    onDismiss()
                }
        )

        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.label_image_picker_gallery),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Theme.custom.primaryText
                )
            },
            leadingContent = {
                Icon(
                    painter = painterResource(R.drawable.photo_library_24),
                    contentDescription = null,
                    tint = Theme.custom.primaryText
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier
                .clickable {
                    onGalleryClick()
                    onDismiss()
                }
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun ImagePickerBottomSheetPreview() {
    InventoryTheme {
        ImagePickerBottomSheetContent(
            onDismiss = {},
            onCameraClick = {},
            onGalleryClick = {}
        )
    }
}