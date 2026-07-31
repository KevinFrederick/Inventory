package com.kevinfreyap.product.presentation.components

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.kevinfreyap.product.R
import com.kevinfreyap.ui.theme.Theme

@Composable
fun AppDatePickerDialog(
    initialDateMillis: Long?,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis
    )

    val colors = DatePickerDefaults.colors(
        containerColor = MaterialTheme.colorScheme.background,
        weekdayContentColor = Theme.custom.primaryText,
        navigationContentColor = Theme.custom.primaryText,
        selectedDayContainerColor = MaterialTheme.colorScheme.primary,

        todayDateBorderColor = Color.Transparent,
        todayContentColor = Theme.custom.primaryText,

        dayContentColor = Theme.custom.primaryText,
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        confirmButton = {
            TextButton(
                enabled = datePickerState.selectedDateMillis != null,
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onDateSelected(millis)
                    }
                    onDismiss()
                },
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
        modifier = modifier
    ) {
        DatePicker(
            state = datePickerState,
            colors = colors,
            title = null,
            headline = null,
            showModeToggle = false
        )
    }
}