package com.kevinfreyap.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.kevinfreyap.ui.R
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.ui.util.DateInputFieldValidationHelper.isValidPartialDate

@Composable
fun AppDateInputDialog(
    dateValue: String,
    fieldLabel: String,
    fieldPlaceholder: String,
    onDateChanged: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    errorMessage: String? = null,
) {
    val focusRequester = remember { FocusRequester() }

    var textFieldValue by remember(dateValue) {
        mutableStateOf(
            TextFieldValue(
                text = dateValue,
                selection = TextRange(dateValue.length)
            ),
            policy = neverEqualPolicy()
        )
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    DatePickerDialog(
        onDismissRequest = onDismiss,
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = stringResource(R.string.btn_label_confirm)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Theme.custom.secondaryText
                )
            ) {
                Text(
                    text = stringResource(R.string.btn_label_cancel)
                )
            }
        },
        properties = DialogProperties(
            decorFitsSystemWindows = false
        ),
        modifier = modifier
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AppTextField(
                value = textFieldValue,
                onValueChange = { newValue ->
                    val onlyDigit = newValue.text.filter { it.isDigit() }

                    if (isValidPartialDate(onlyDigit)) {
                        val lockedCursorValue = newValue.copy(
                            selection = TextRange(newValue.text.length)
                        )

                        textFieldValue = lockedCursorValue
                        onDateChanged(lockedCursorValue.text)
                    } else {
                        textFieldValue = textFieldValue.copy(
                            selection = TextRange(textFieldValue.text.length)
                        )
                    }

                },
                minLines = 1,
                maxLines = 1,
                unfocusedColor = Theme.custom.hint,
                label = fieldLabel,
                placeholder = fieldPlaceholder,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
                visualTransformation = visualTransformation,
                isError = isError,
                errorMessage = errorMessage,
                modifier = Modifier
                    .focusRequester(focusRequester)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}