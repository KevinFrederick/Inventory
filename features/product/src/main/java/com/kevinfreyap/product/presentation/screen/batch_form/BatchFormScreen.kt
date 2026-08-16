package com.kevinfreyap.product.presentation.screen.batch_form

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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kevinfreyap.product.R
import com.kevinfreyap.product.presentation.action.ProductFormAction
import com.kevinfreyap.product.presentation.navigation.BatchFormNavigation
import com.kevinfreyap.product.presentation.screen.add_product.section.DialogSummaryList
import com.kevinfreyap.product.presentation.screen.add_product.section.SectionBatchDetail
import com.kevinfreyap.product.presentation.screen.add_product.section.SectionBatchInformation
import com.kevinfreyap.product.presentation.screen.batch_form.section.SectionBatchHeadline
import com.kevinfreyap.product.presentation.state.ScreenBatchFormState
import com.kevinfreyap.ui.R as coreR
import com.kevinfreyap.ui.components.AppCenterTopBar
import com.kevinfreyap.ui.components.AppOutlinedButton
import com.kevinfreyap.ui.components.AppPrimaryButton
import com.kevinfreyap.ui.components.AppTextDialog
import com.kevinfreyap.ui.components.AppTextIconDialog
import com.kevinfreyap.ui.event.UiEvent
import com.kevinfreyap.ui.state.UiState
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun BatchFormScreen(
    modifier: Modifier = Modifier,
    onNavigate: (BatchFormNavigation) -> Unit,
    viewModel: BatchFormViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val state by viewModel.formState.collectAsStateWithLifecycle()

    var showDiscardDialog by rememberSaveable { mutableStateOf(false) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    val handleBackNavigation = {
        if (state.hasUnsavedChanges) {
            showDiscardDialog = true
        } else {
            onNavigate(BatchFormNavigation.NavigateUp)
        }
    }

    LaunchedEffect(true) {
        viewModel.uiEvent.collect { event ->
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

    BackHandler(enabled = state.hasUnsavedChanges) {
        showDiscardDialog = true
    }

    BatchFormContent(
        state = state,
        showDiscardDialog = showDiscardDialog,
        showDeleteDialog = showDeleteDialog,
        handleBackNavigation = handleBackNavigation,
        onDismissDiscardDialog = {
            showDiscardDialog = false
        },
        onShowDeleteDialog = {
            showDeleteDialog = true
        },
        onDeleteDialogAction = { isConfirm ->
            if (isConfirm) {
                viewModel.deleteBatch()
                showDeleteDialog = false
            } else {
                showDeleteDialog = false
            }
        },
        onAction = viewModel::onAction,
        onNavigate = onNavigate,
        modifier = modifier
    )
}

@Composable
fun BatchFormContent(
    state: ScreenBatchFormState,
    showDiscardDialog: Boolean,
    showDeleteDialog: Boolean,
    handleBackNavigation: () -> Unit,
    onDismissDiscardDialog: () -> Unit,
    onShowDeleteDialog: () -> Unit,
    onDeleteDialogAction: (isConfirm: Boolean) -> Unit,
    onAction: (ProductFormAction) -> Unit,
    onNavigate: (BatchFormNavigation) -> Unit,
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
                title = if (state.isReadOnly) {
                    stringResource(R.string.title_batch_detail)
                } else if (state.isExistingBatch) {
                    stringResource(R.string.title_edit_batch)
                } else {
                    stringResource(R.string.title_add_batch)
                },
                onBackClick = handleBackNavigation,
                isLoading = state.uiState is UiState.Loading,
                actionButton = {
                    if (state.isExistingBatch) {
                        IconButton(
                            onClick = onShowDeleteDialog,
                        ) {
                            Icon(
                                painter = painterResource(coreR.drawable.delete_24),
                                contentDescription = "Delete product",
                                tint = Theme.custom.secondaryText,
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (state.isReadOnly) {
                FloatingActionButton(
                    onClick = {
                        onAction(ProductFormAction.BatchToggleAction.ToggleEditMode)
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        painter = painterResource(coreR.drawable.edit_24),
                        contentDescription = stringResource(R.string.btn_label_edit)
                    )
                }
            }
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
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                SectionBatchHeadline(
                    productName = state.productName,
                    batchShortId = state.batchShortId
                )

                SectionBatchDetail(
                    batchFormDetailState = state.batchDetail,
                    formErrors = state.formErrors,
                    onAction = onAction,
                    isReadOnly = state.isReadOnly
                )

                SectionBatchInformation(
                    batchFormInformationState = state.batchInformation,
                    formErrors = state.formErrors,
                    onAction = onAction,
                    isReadOnly = state.isReadOnly
                )
            }

            if (!state.isReadOnly) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    AppPrimaryButton(
                        text = stringResource(R.string.btn_label_save_batch),
                        enabled = state.isSavedEnabled && state.uiState !is UiState.Loading,
                        onClick = {
                            onAction(ProductFormAction.Save)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(coreR.drawable.check_24),
                                contentDescription = stringResource(R.string.btn_label_save_batch),
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }

    if (state.showSummaryConfirmationDialog) {
        DialogSummaryList(
            title = stringResource(R.string.dialog_title_review_value),
            subtitle = stringResource(R.string.dialog_subtitle_review_value),
            formErrors = state.formErrors,
            productDetail = null,
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
                        onNavigate(BatchFormNavigation.NavigateUp)
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

    if (showDeleteDialog) {
        AppTextIconDialog(
            icon = painterResource(R.drawable.custom_warning_icon),
            title = stringResource(R.string.dialog_title_delete_batch),
            subtitle = stringResource(R.string.dialog_subtitle_delete_batch),
            iconColor = MaterialTheme.colorScheme.error,
            onDismissRequest = {
                onDeleteDialogAction(
                    false
                )
            },
            positiveBtn = {
                AppPrimaryButton(
                    text = stringResource(R.string.btn_label_delete),
                    onClick = {
                        onDeleteDialogAction(
                            true
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(coreR.drawable.delete_24),
                            contentDescription = null,
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            negativeBtn = {
                AppOutlinedButton(
                    text = stringResource(R.string.btn_label_cancel),
                    onClick = {
                        onDeleteDialogAction(
                            false
                        )
                    },
                    borderColor = Theme.custom.hint,
                    contentColor = Theme.custom.hint,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun BatchFormScreenPreview() {
    InventoryTheme {
        BatchFormContent(
            state = ScreenBatchFormState(
                productName = "Smartphone",
                batchShortId = "123AB3",
                isReadOnly = true,
                isExistingBatch = true
            ),
            onAction = {},
            showDiscardDialog = false,
            handleBackNavigation = {  },
            onDismissDiscardDialog = {  },
            onNavigate = {  },
            showDeleteDialog = false,
            onDeleteDialogAction = {},
            onShowDeleteDialog = {}
        )
    }
}