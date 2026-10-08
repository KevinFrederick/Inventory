package com.kevinfreyap.user.presentation.screen.edit_profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.ui.components.AppCenterTopBar
import com.kevinfreyap.ui.components.AppImageUpload
import com.kevinfreyap.ui.image_picker.ImagePickerBottomSheet
import com.kevinfreyap.ui.state.UiState
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.user.R
import com.kevinfreyap.ui.R as coreR

@Composable
fun EditProfileScreen(
    modifier:  Modifier = Modifier
) {

}

@Composable
fun EditProfileContent(
    modifier: Modifier = Modifier
) {
    var showImagePickerBottomSheet by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppCenterTopBar(
                title = stringResource(R.string.title_edit_profile),
                onBackClick = {},
//                isLoading = state.uiState is UiState.Loading,
            )
        }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .padding(innerPadding)
                .padding(
                    top = 12.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            AppImageUpload(
                icon = painterResource(coreR.drawable.add_a_photo_24),
                label = stringResource(R.string.label_upload_profile_pic),
                uriString = "",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    showImagePickerBottomSheet = true
                },
                onRemove = {}
            )
        }
    }

    if (showImagePickerBottomSheet) {
        ImagePickerBottomSheet(
            onDismiss = {
                showImagePickerBottomSheet = false
            },
            onCameraClick = {
            },
            onGalleryClick = {
            }
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun EditProfileScreenPreview() {
    InventoryTheme {
        EditProfileContent()
    }
}