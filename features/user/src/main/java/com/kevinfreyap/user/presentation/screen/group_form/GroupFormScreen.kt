package com.kevinfreyap.user.presentation.screen.group_form

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.kevinfreyap.ui.components.AppCenterTopBar
import com.kevinfreyap.ui.components.AppOutlinedButton
import com.kevinfreyap.ui.components.AppPrimaryButton
import com.kevinfreyap.ui.components.AppTextDialog
import com.kevinfreyap.ui.event.UiEvent
import com.kevinfreyap.ui.state.UiState
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.user.R
import com.kevinfreyap.user.presentation.action.GroupFormAction
import com.kevinfreyap.user.presentation.navigation.GroupFormNavigation
import com.kevinfreyap.user.presentation.screen.group_form.section.SectionGroupForm
import com.kevinfreyap.user.presentation.state.GroupFormDetailState
import com.kevinfreyap.user.presentation.state.GroupFormScreenState

@Composable
fun GroupFormScreen(
    onNavigate: (GroupFormNavigation) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GroupFormViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val state by viewModel.formState.collectAsStateWithLifecycle()
    var showDiscardDialog by rememberSaveable { mutableStateOf(false) }

    val handleBackNavigation = {
        if (state.hasUnsavedChanges) {
            showDiscardDialog = true
        } else {
            onNavigate(GroupFormNavigation.NavigateUp)
        }
    }

    BackHandler(enabled = state.hasUnsavedChanges) {
        showDiscardDialog = true
    }

    LaunchedEffect(viewModel.uiEvent, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEvent.collect { event ->
                when (event) {
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

    GroupFormContent(
        state = state,
        showDiscardDialog = showDiscardDialog,
        onDiscardDialogToggled = { isShow ->
            showDiscardDialog = isShow
        },
        onBackClicked = handleBackNavigation,
        onNavigate = onNavigate,
        onAction = { action ->
            viewModel.onAction(action)
        },
        modifier = modifier
    )
}

@Composable
fun GroupFormContent(
    state: GroupFormScreenState,
    showDiscardDialog: Boolean,
    onDiscardDialogToggled: (Boolean) -> Unit,
    onBackClicked: () -> Unit,
    onNavigate: (GroupFormNavigation) -> Unit,
    onAction: (GroupFormAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            AppCenterTopBar(
                title = "",
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
                    text = stringResource(
                        if (state.isEditMode) R.string.btn_label_update_group else R.string.btn_label_create_group
                    ),
                    onClick = {
                        onAction(GroupFormAction.Submit)
                    },
                    enabled = state.hasUnsavedChanges,
                    modifier = Modifier.fillMaxWidth()
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(
                    top = 4.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .imePadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(
                        if (state.isEditMode) R.string.title_edit_group else R.string.title_add_group
                    ),
                    style = MaterialTheme.typography.titleLarge,
                    color = Theme.custom.primaryText
                )

                Text(
                    text = stringResource(
                        if (state.isEditMode) R.string.label_subtitle_edit_group else R.string.label_subtitle_add_group
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Theme.custom.secondaryText
                )
            }

            SectionGroupForm(
                state = state.groupDetail,
                errors = state.formErrors,
                onAction = onAction
            )
        }
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
                        onNavigate(GroupFormNavigation.NavigateUp)
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

@Preview(showBackground = true)
@Composable
fun GroupFormScreenAddPreview() {
    InventoryTheme {
        GroupFormContent(
            state = GroupFormScreenState(
                isEditMode = false,
                groupDetail = GroupFormDetailState()
            ),
            showDiscardDialog = false,
            onNavigate = {},
            onAction = {},
            onDiscardDialogToggled = {},
            onBackClicked = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GroupFormScreenEditPreview() {
    InventoryTheme {
        GroupFormContent(
            state = GroupFormScreenState(
                isEditMode = true,
                originalGroupDetail = GroupFormDetailState(
                    name = "Existing Group",
                    description = "Some description",
                    address = "123 Street"
                ),
                groupDetail = GroupFormDetailState(
                    name = "Existing Group",
                    description = "Some description",
                    address = "123 Street"
                )
            ),
            showDiscardDialog = false,
            onNavigate = {},
            onAction = {},
            onDiscardDialogToggled = {},
            onBackClicked = {},
        )
    }
}