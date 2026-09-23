package com.kevinfreyap.product.presentation.components

import android.content.pm.PackageManager
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
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

class ImagePickerState(
    val launchCamera: (Uri) -> Unit,
    val launchGallery: () -> Unit
)

@Composable
fun rememberImagePicker (
    onImagePicked: (String) -> Unit
): ImagePickerState {
    val context = LocalContext.current

    var tempCameraUri by rememberSaveable { mutableStateOf<String?>(null) }
    var pendingUri by remember { mutableStateOf<Uri?>(null) }

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

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted && pendingUri != null) {
            tempCameraUri = pendingUri.toString()
            cameraLauncher.launch(pendingUri!!)
        }
    }

    return remember {
        ImagePickerState(
            launchCamera = { uri ->
                val hasPermission = ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED

                if (hasPermission) {
                    tempCameraUri = uri.toString()
                    cameraLauncher.launch(uri)
                } else {
                    pendingUri = uri
                    permissionLauncher.launch(android.Manifest.permission.CAMERA)
                }

            },
            launchGallery = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        )
    }
}