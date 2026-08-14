package com.kevinfreyap.product.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

class ImagePickerState(
    val launchCamera: (Uri) -> Unit,
    val launchGallery: () -> Unit
)

@Composable
fun rememberImagePicker (
    onImagePicked: (String) -> Unit
): ImagePickerState {
    var tempCameraUri by rememberSaveable { mutableStateOf<String?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                onImagePicked(uri.toString())
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success && tempCameraUri != null) {
                onImagePicked(tempCameraUri!!)
            }
        }
    )

    return remember {
        ImagePickerState(
            launchCamera = { uri ->
                tempCameraUri = uri.toString()
                cameraLauncher.launch(uri)
            },
            launchGallery = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        )
    }
}