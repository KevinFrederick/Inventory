package com.kevinfreyap.scanner.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.kevinfreyap.scanner.R
import com.kevinfreyap.ui.R as coreR
import com.kevinfreyap.scanner.analyzer.BarcodeAnalyzer
import com.kevinfreyap.scanner.navigation.ScannerNavigation
import com.kevinfreyap.ui.components.AppPrimaryButton
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun ScannerScreen (
    onScanSuccess: (String, String) -> Unit,
    onNavigate: (ScannerNavigation) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)

            setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(context),
                BarcodeAnalyzer { (scannedValue, format) ->
                    unbind()
                    onScanSuccess(scannedValue, format)
                }
            )
        }
    }

    BackHandler {
        onNavigate(ScannerNavigation.NavigateUp)
    }

    ScannerContent(
        lifecycleOwner = lifecycleOwner,
        cameraController = cameraController,
        hasCameraPermission = hasCameraPermission,
        onLauncherClick = {
            permissionLauncher.launch(
                Manifest.permission.CAMERA
            )
        },
        onNavigate = onNavigate,
        modifier = modifier
    )
}

@Composable
fun ScannerContent(
    lifecycleOwner: LifecycleOwner,
    cameraController: LifecycleCameraController?,
    hasCameraPermission: Boolean,
    onLauncherClick: () -> Unit,
    onNavigate: (ScannerNavigation) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        if (hasCameraPermission) {
            if (cameraController != null) {
                AndroidView(
                    factory = { context ->
                        PreviewView(context).apply {
                            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                            scaleType = PreviewView.ScaleType.FILL_CENTER
                            controller = cameraController
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize(),
                    update = {
                        cameraController.bindToLifecycle(lifecycleOwner)
                    }
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Camera Preview Placeholder",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.Black.copy(0.6f))
                    .padding(24.dp)
            ) {
                Text(
                    text = stringResource(R.string.label_camera_barcode),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Icon(
                    painter = painterResource(coreR.drawable.camera_24),
                    contentDescription = "Camera",
                    tint = Theme.custom.hint,
                    modifier = Modifier
                        .size(128.dp)
                )

                Text(
                    text = stringResource(R.string.label_permission_camera),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(24.dp))
                
                AppPrimaryButton(
                    onClick = onLauncherClick,
                    text = stringResource(R.string.btn_label_grant_permission),
                )
            }
        }

        IconButton(
            onClick = {
                onNavigate(ScannerNavigation.NavigateUp)
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface.copy(0.6f),
                    shape = CircleShape
                )
        ) {
            Icon(
                painter = painterResource(coreR.drawable.arrow_left_24),
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
@Preview(
    showBackground = true
)
fun ScannerScreenPreview_Granted() {
    InventoryTheme {
        ScannerContent(
            lifecycleOwner = LocalLifecycleOwner.current,
            cameraController = null,
            hasCameraPermission = true,
            onLauncherClick = {},
            onNavigate = {}
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun ScannerScreenPreview_Denied() {
    InventoryTheme {
        ScannerContent(
            lifecycleOwner = LocalLifecycleOwner.current,
            cameraController = null,
            hasCameraPermission = false,
            onLauncherClick = {},
            onNavigate = {}
        )
    }
}