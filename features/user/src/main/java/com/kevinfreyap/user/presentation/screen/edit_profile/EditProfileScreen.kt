package com.kevinfreyap.user.presentation.screen.edit_profile

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.kevinfreyap.domain.error.ImageError
import com.kevinfreyap.ui.components.AppCenterTopBar
import com.kevinfreyap.ui.components.AppImageUpload
import com.kevinfreyap.ui.components.AppOutlinedButton
import com.kevinfreyap.ui.components.AppPrimaryButton
import com.kevinfreyap.ui.components.AppTextDialog
import com.kevinfreyap.ui.event.UiEvent
import com.kevinfreyap.ui.image_picker.ImagePickerBottomSheet
import com.kevinfreyap.ui.image_picker.rememberImagePicker
import com.kevinfreyap.ui.state.UiState
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.user.R
import com.kevinfreyap.user.presentation.action.EditProfileAction
import com.kevinfreyap.user.presentation.navigation.EditProfileNavigation
import com.kevinfreyap.user.presentation.screen.edit_profile.section.SectionUserDetail
import com.kevinfreyap.user.presentation.state.EditProfileScreenState
import java.io.File
import com.kevinfreyap.ui.R as coreR

@Composable
fun EditProfileScreen(
    onNavigate: (EditProfileNavigation) -> Unit,
    modifier:  Modifier = Modifier,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val state by viewModel.formState.collectAsStateWithLifecycle()
    var showDiscardDialog by rememberSaveable { mutableStateOf(false) }

    val imagePicker = rememberImagePicker (
        onImagePicked = { uriString ->
            viewModel.onAction(EditProfileAction.OnImageUrlChanged(uriString))
        }
    )

    val handleBackNavigation = {
        if (state.hasUnsavedChanges) {
            showDiscardDialog = true
        } else {
            onNavigate(EditProfileNavigation.NavigateUp)
        }
    }

    BackHandler(enabled = state.hasUnsavedChanges) {
        showDiscardDialog = true
    }

    LaunchedEffect(viewModel.uiEvent, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEvent.collect { event ->
                when(event) {
                    is UiEvent.Navigate -> {
                        onNavigate(event.destination)
                    }
                    is UiEvent.ShowToast -> {
                        Toast.makeText(
                            context,
                            event.asString(context),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    EditProfileContent(
        state = state,
        showDiscardDialog = showDiscardDialog,
        onDiscardDialogToggled = { isShow ->
            showDiscardDialog = isShow
        },
        onBackClicked = handleBackNavigation,
        onNavigate = onNavigate,
        onAction = {action ->
            when(action) {
                is EditProfileAction.ProfilePictureAction.OnGalleryClicked -> {
                    imagePicker.launchGallery()
                }
                is EditProfileAction.ProfilePictureAction.OnCameraClicked -> {
                    val tempFile = File.createTempFile("profile_img_", ".jpg", context.cacheDir)

                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        tempFile
                    )

                    imagePicker.launchCamera(uri)
                }
                is EditProfileAction.ProfilePictureAction.OnRemoveImage -> {
                    viewModel.onAction(EditProfileAction.OnImageUrlChanged(null))
                }

                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier
    )
}

@Composable
fun EditProfileContent(
    state: EditProfileScreenState,
    showDiscardDialog: Boolean,
    onDiscardDialogToggled: (Boolean) -> Unit,
    onBackClicked: () -> Unit,
    onNavigate: (EditProfileNavigation) -> Unit,
    onAction: (EditProfileAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    var showImagePickerBottomSheet by rememberSaveable { mutableStateOf(false) }
    var showRemoveImageDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppCenterTopBar(
                title = stringResource(R.string.title_edit_profile),
                onBackClick = onBackClicked,
                isLoading = state.uiState is UiState.Loading,
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                AppPrimaryButton(
                    text = stringResource(R.string.btn_label_update_profile),
                    onClick = {
                        onAction(EditProfileAction.UpdateProfile)
                    },
                    enabled = state.hasUnsavedChanges,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        },
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                    }
                )
            },
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
                .imePadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            AppImageUpload(
                icon = painterResource(coreR.drawable.add_a_photo_24),
                label = stringResource(R.string.label_upload_profile_pic),
                uriString = state.userDetail.profileUrl,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    showImagePickerBottomSheet = true
                },
                onRemove = {
                    showRemoveImageDialog = true
                },
                isError = state.formErrors?.urlError != null,
                errorMessage = if (state.formErrors?.urlError != null) {
                    when (state.formErrors.urlError) {
                        ImageError.INVALID_FORMAT -> stringResource(coreR.string.error_product_image_invalid_format)
                        ImageError.PATH_TOO_LONG -> stringResource(coreR.string.error_product_image_path_too_long)
                        ImageError.FILE_TOO_LARGE -> stringResource(coreR.string.error_product_image_file_too_large)
                    }
                } else null
            )

            SectionUserDetail(
                state = state.userDetail,
                errors = state.formErrors,
                onAction = onAction
            )
        }
    }

    if (showImagePickerBottomSheet) {
        ImagePickerBottomSheet(
            onDismiss = {
                showImagePickerBottomSheet = false
            },
            onCameraClick = {
                showImagePickerBottomSheet = false
                onAction(EditProfileAction.ProfilePictureAction.OnCameraClicked)
            },
            onGalleryClick = {
                showImagePickerBottomSheet = false
                onAction(EditProfileAction.ProfilePictureAction.OnGalleryClicked)
            }
        )
    }

    if (showRemoveImageDialog) {
        AppTextDialog(
            title = stringResource(R.string.dialog_label_title_remove_image),
            subtitle = stringResource(R.string.dialog_label_subtitle_remove_image),
            onDismissRequest = {
                showRemoveImageDialog = false
            },
            positiveBtn = {
                AppPrimaryButton(
                    text = stringResource(R.string.btn_label_confirm),
                    onClick = {
                        showRemoveImageDialog = false
                        onAction(EditProfileAction.ProfilePictureAction.OnRemoveImage)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            negativeBtn = {
                AppOutlinedButton(
                    text = stringResource(R.string.btn_label_cancel),
                    onClick = {
                        showRemoveImageDialog = false
                    },
                    borderColor = Theme.custom.hint,
                    contentColor = Theme.custom.hint,
                    modifier = Modifier.fillMaxWidth()
                )
            },
        )
    }

    if (showDiscardDialog) {
        AppTextDialog(
            title = stringResource(R.string.dialog_label_title_discard_changes),
            subtitle = stringResource(R.string.dialog_label_subtitle_discard_changes),
            onDismissRequest = {
                onDiscardDialogToggled(false)
            },
            positiveBtn = {
                AppPrimaryButton(
                    text = stringResource(R.string.btn_label_discard),
                    onClick = {
                        onNavigate(EditProfileNavigation.NavigateUp)
                        onDiscardDialogToggled(false)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            negativeBtn = {
                AppOutlinedButton(
                    text = stringResource(R.string.btn_label_keep_edit),
                    onClick = {
                        onDiscardDialogToggled(false)
                    },
                    borderColor = Theme.custom.hint,
                    contentColor = Theme.custom.hint,
                    modifier = Modifier.fillMaxWidth()
                )
            },
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun EditProfileScreenPreview() {
    InventoryTheme {
        EditProfileContent(
            state = EditProfileScreenState(),
            showDiscardDialog = false,
            onNavigate = {},
            onAction = {},
            onDiscardDialogToggled = { },
            onBackClicked = {  },
        )
    }
}