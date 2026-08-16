package com.kevinfreyap.product.presentation.screen.add_product

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ScrollState
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kevinfreyap.product.R
import com.kevinfreyap.product.domain.model.error.BatchPriceError
import com.kevinfreyap.product.domain.model.error.BatchQuantityError
import com.kevinfreyap.product.domain.model.error.ProductFormError
import com.kevinfreyap.product.domain.model.error.ProductImageError
import com.kevinfreyap.product.domain.model.error.ProductMinimumQuantityError
import com.kevinfreyap.ui.R as coreR
import com.kevinfreyap.product.presentation.action.ProductFormAction
import com.kevinfreyap.product.presentation.components.TextSwitchRow
import com.kevinfreyap.product.presentation.components.rememberImagePicker
import com.kevinfreyap.product.presentation.navigation.AddProductNavigation
import com.kevinfreyap.product.presentation.screen.add_product.section.DialogSummaryList
import com.kevinfreyap.product.presentation.screen.add_product.section.SectionBatchDetail
import com.kevinfreyap.product.presentation.screen.add_product.section.SectionBatchInformation
import com.kevinfreyap.product.presentation.screen.add_product.section.SectionProductIdentification
import com.kevinfreyap.product.presentation.screen.add_product.section.SectionProductInformation
import com.kevinfreyap.product.presentation.screen.bottom_sheet.ImagePickerBottomSheet
import com.kevinfreyap.product.presentation.state.ScreenAddProductState
import com.kevinfreyap.product.presentation.state.BatchFormDetailState
import com.kevinfreyap.product.presentation.state.ProductFormDetailState
import com.kevinfreyap.ui.components.AppCenterTopBar
import com.kevinfreyap.ui.components.AppImageUpload
import com.kevinfreyap.ui.components.AppOutlinedButton
import com.kevinfreyap.ui.components.AppPrimaryButton
import com.kevinfreyap.ui.components.AppTextDialog
import com.kevinfreyap.ui.components.AppTextIconDialog
import com.kevinfreyap.ui.event.UiEvent
import com.kevinfreyap.ui.state.UiState
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import kotlinx.coroutines.delay
import java.io.File
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun AddProductScreen(
    modifier: Modifier = Modifier,
    onNavigate: (AddProductNavigation) -> Unit,
    viewmodel: AddProductViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewmodel.formState.collectAsStateWithLifecycle()

    var showImagePickerBottomSheet by rememberSaveable { mutableStateOf(false) }
    var showRemoveImageDialog by rememberSaveable { mutableStateOf(false) }
    var showDiscardDialog by rememberSaveable { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    val imagePicker = rememberImagePicker (
        onImagePicked = { uriString ->
            viewmodel.onAction(ProductFormAction.ProductDetailAction.OnImageUriChanged(uriString))
        }
    )

    val handleBackNavigation = {
        if (state.hasUnsavedChanges) {
            showDiscardDialog = true
        } else {
            onNavigate(AddProductNavigation.NavigateUp)
        }
    }

    LaunchedEffect(true) {
        viewmodel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.Navigate -> {
                    onNavigate(event.destination)
                }
                is UiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.messageRes,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    LaunchedEffect(state.batchDetail.addInitialStock) {
        if (state.batchDetail.addInitialStock) {
            delay(150.milliseconds)

            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    BackHandler(enabled = state.hasUnsavedChanges) {
        showDiscardDialog = true
    }

    AddProductContent(
        state = state,
        scrollState = scrollState,
        showImagePickerBottomSheet = showImagePickerBottomSheet,
        showRemoveImageDialog = showRemoveImageDialog,
        showDiscardDialog = showDiscardDialog,
        handleBackNavigation = handleBackNavigation,
        onDismissDiscardDialog = {
            showDiscardDialog = false
        },
        onAction = {action ->
            when(action) {
                is ProductFormAction.ImagePickerAction.OnUploadClick -> {
                    showImagePickerBottomSheet = true
                }
                is ProductFormAction.ImagePickerAction.OnDismissSheet -> {
                    showImagePickerBottomSheet = false
                }
                is ProductFormAction.ImagePickerAction.OnGalleryClick -> {
                    showImagePickerBottomSheet = false
                    imagePicker.launchGallery()
                }
                is ProductFormAction.ImagePickerAction.OnCameraClick -> {
                    showImagePickerBottomSheet = false

                    val tempFile = File.createTempFile("product_img_", ".jpg", context.cacheDir)

                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        tempFile
                    )

                    imagePicker.launchCamera(uri)
                }
                is ProductFormAction.ImagePickerAction.OnRemoveImage -> {
                    showRemoveImageDialog = true
                }
                is ProductFormAction.ImagePickerAction.OnDismissRemoveDialog -> {
                    showRemoveImageDialog = false
                }
                is ProductFormAction.ImagePickerAction.OnConfirmRemoveDialog -> {
                    viewmodel.onAction(ProductFormAction.ProductDetailAction.OnImageUriChanged(null))
                    showRemoveImageDialog = false
                }
                else -> viewmodel.onAction(action)
            }
        },
        onNavigate = onNavigate,
        modifier = modifier
    )
}

@Composable
fun AddProductContent(
    state: ScreenAddProductState,
    scrollState: ScrollState,
    showImagePickerBottomSheet: Boolean,
    showRemoveImageDialog: Boolean,
    showDiscardDialog: Boolean,
    handleBackNavigation: () -> Unit,
    onDismissDiscardDialog: () -> Unit,
    onAction: (ProductFormAction) -> Unit,
    onNavigate: (AddProductNavigation) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        focusManager.clearFocus()
                    }
                )
            },
        topBar = {
            AppCenterTopBar(
                title = stringResource(R.string.title_add_product),
                onBackClick = handleBackNavigation,
                isLoading = state.uiState is UiState.Loading,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .imePadding()
                .fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                AppImageUpload(
                    uriString = state.productDetail.productImageUriString,
                    icon = painterResource(R.drawable.add_a_photo_24),
                    label = stringResource(R.string.btn_label_upload_image),
                    onClick = {
                        onAction(ProductFormAction.ImagePickerAction.OnUploadClick)
                    },
                    onRemove = {
                        onAction(ProductFormAction.ImagePickerAction.OnRemoveImage)
                    },
                    isError = state.formErrors?.imageError != null,
                    errorMessage = if (state.formErrors?.imageError != null) {
                        when (state.formErrors.imageError) {
                            ProductImageError.INVALID_FORMAT -> stringResource(R.string.error_product_image_invalid_format)
                            ProductImageError.PATH_TOO_LONG -> stringResource(R.string.error_product_image_path_too_long)
                            ProductImageError.FILE_TOO_LARGE -> stringResource(R.string.error_product_image_file_too_large)
                        }
                    } else null
                )

                SectionProductInformation(
                    productDetailState = state.productDetail,
                    formErrors = state.formErrors,
                    onAction = onAction,
                )

                SectionProductIdentification(
                    productFormIdentificationState = state.productIdentification,
                    formErrors = state.formErrors,
                    onAction = onAction,
                )

                TextSwitchRow(
                    text = stringResource(R.string.label_add_initial_stock),
                    subtitle = stringResource(R.string.label_initial_stock_subtitle),
                    isChecked = state.batchDetail.addInitialStock,
                    onClick = { isChecked ->
                        onAction(ProductFormAction.BatchDetailAction.OnAddInitialStockToggled(isChecked))
                    },
                )

                AnimatedVisibility(
                    visible = state.batchDetail.addInitialStock
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        SectionBatchDetail(
                            batchFormDetailState = state.batchDetail,
                            formErrors = state.formErrors,
                            onAction = onAction,
                        )

                        SectionBatchInformation(
                            batchFormInformationState = state.batchInformation,
                            formErrors = state.formErrors,
                            onAction = onAction
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                AppPrimaryButton(
                    text = stringResource(R.string.btn_label_save_product),
                    enabled = state.isSavedEnabled,
                    onClick = {
                        onAction(ProductFormAction.Save)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(coreR.drawable.check_24),
                            contentDescription = stringResource(R.string.btn_label_save_product),
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }
    }

    if (showImagePickerBottomSheet) {
        ImagePickerBottomSheet(
            onDismiss = {
                onAction(ProductFormAction.ImagePickerAction.OnDismissSheet)
            },
            onCameraClick = {
                onAction(ProductFormAction.ImagePickerAction.OnCameraClick)
            },
            onGalleryClick = {
                onAction(ProductFormAction.ImagePickerAction.OnGalleryClick)
            }
        )
    }

    if (showRemoveImageDialog) {
        AppTextDialog(
            title = stringResource(R.string.dialog_title_remove_image),
            subtitle = stringResource(R.string.dialog_subtitle_remove_image),
            onDismissRequest = {
                onAction(ProductFormAction.ImagePickerAction.OnDismissRemoveDialog)
            },
            positiveBtn = {
                AppPrimaryButton(
                    text = stringResource(R.string.btn_label_confirm),
                    onClick = {
                        onAction(ProductFormAction.ImagePickerAction.OnConfirmRemoveDialog)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            negativeBtn = {
                AppOutlinedButton(
                    text = stringResource(R.string.btn_label_cancel),
                    onClick = {
                        onAction(ProductFormAction.ImagePickerAction.OnDismissRemoveDialog)
                    },
                    borderColor = Theme.custom.hint,
                    contentColor = Theme.custom.hint,
                    modifier = Modifier.fillMaxWidth()
                )
            },
        )
    }

    if (state.showSummaryConfirmationDialog) {
        DialogSummaryList(
            title = stringResource(R.string.dialog_title_review_value),
            subtitle = stringResource(R.string.dialog_subtitle_review_value),
            formErrors = state.formErrors,
            productDetail = state.productDetail,
            batchDetail = state.batchDetail,
            onDismissRequest = {
                onAction(ProductFormAction.SummaryDialogAction.OnDismissWarningsDialog)
            },
            positiveBtn = {
                AppPrimaryButton(
                    text = stringResource(R.string.btn_label_confirm_all),
                    onClick = {
                        onAction(ProductFormAction.SummaryDialogAction.OnConfirmAllWarnings)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            negativeBtn = {
                AppOutlinedButton(
                    text = stringResource(R.string.btn_label_edit),
                    onClick = {
                        onAction(ProductFormAction.SummaryDialogAction.OnDismissWarningsDialog)
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
            title = stringResource(R.string.dialog_title_discard_changes),
            subtitle = stringResource(R.string.dialog_subtitle_discard_changes),
            onDismissRequest = onDismissDiscardDialog,
            positiveBtn = {
                AppPrimaryButton(
                    text = stringResource(R.string.btn_label_discard),
                    onClick = {
                        onNavigate(AddProductNavigation.NavigateUp)
                        onDismissDiscardDialog()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            negativeBtn = {
                AppOutlinedButton(
                    text = stringResource(R.string.btn_label_keep_edit),
                    onClick = onDismissDiscardDialog,
                    borderColor = Theme.custom.hint,
                    contentColor = Theme.custom.hint,
                    modifier = Modifier.fillMaxWidth()
                )
            },
        )
    }

    if (state.uiState is UiState.Error) {
        AppTextIconDialog(
            icon = painterResource(R.drawable.custom_error_load_icon),
            title = stringResource(R.string.dialog_title_save_failed),
            subtitle = stringResource(R.string.dialog_subtitle_save_failed),
            iconColor = MaterialTheme.colorScheme.error,
            iconSize = 160.dp,
            onDismissRequest = {
                onAction(ProductFormAction.StatusDialogAction.OnDismissError)
            },
            positiveBtn = {
                AppPrimaryButton(
                    text = stringResource(R.string.btn_label_close),
                    onClick = {
                        onAction(ProductFormAction.StatusDialogAction.OnDismissError)
                    },
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
            },
        )
    }
}

@Preview (
    showBackground = true,
    device = "spec:width=1080px,height=1900px,dpi=416"
)
@Composable
fun AddProductContentPreview() {
    InventoryTheme {
        var isChecked by remember { mutableStateOf(true) }

        AddProductContent(
            state = ScreenAddProductState(),
            scrollState = rememberScrollState(),
            showImagePickerBottomSheet = false,
            showRemoveImageDialog = false,
            showDiscardDialog = false,
            handleBackNavigation = {},
            onDismissDiscardDialog = {},
            onAction = {action ->
                if (action is ProductFormAction.BatchDetailAction.OnAddInitialStockToggled) {
                    isChecked = !isChecked
                }
            },
            onNavigate = {}
        )
    }
}

@Preview (
    showBackground = true,
    device = "spec:width=1080px,height=3600px,dpi=416"
)
@Composable
fun AddProductContentPreview_Filled() {
    InventoryTheme {
        var isChecked by remember { mutableStateOf(true) }

        AddProductContent(
            state = ScreenAddProductState(
                showSummaryConfirmationDialog = false,
                batchDetail = BatchFormDetailState(
                    addInitialStock = isChecked,
                    batchQuantity = "11000",
                    batchPrice = "1000000000"
                ),
                productDetail = ProductFormDetailState(
                    productMinQuantity = "10000"
                ),
                formErrors = ProductFormError(
                    minQuantityError = ProductMinimumQuantityError.REQUIRES_CONFIRMATION,
                    quantityError = BatchQuantityError.REQUIRES_CONFIRMATION,
                    priceError = BatchPriceError.REQUIRES_CONFIRMATION
                )
            ),
            scrollState = rememberScrollState(),
            showImagePickerBottomSheet = false,
            showRemoveImageDialog = false,
            showDiscardDialog = false,
            handleBackNavigation = {},
            onDismissDiscardDialog = {},
            onAction = {action ->
                if (action is ProductFormAction.BatchDetailAction.OnAddInitialStockToggled) {
                    isChecked = !isChecked
                }
            },
            onNavigate = {}
        )
    }
}