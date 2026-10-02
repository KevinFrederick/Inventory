package com.kevinfreyap.auth.presentation.screen.auth.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kevinfreyap.auth.R
import com.kevinfreyap.ui.R as coreR
import com.kevinfreyap.ui.components.AppOutlinedButton
import com.kevinfreyap.ui.components.AppPrimaryButton
import com.kevinfreyap.ui.components.AppTextField
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun ForgotPasswordDialog(
    email: String,
    onEmailChange: (String) -> Unit,
    onSendDialog: () -> Unit,
    onDismissDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = {}
    ) {
        ForgotPasswordDialogContent(
            email = email,
            onEmailChange = onEmailChange,
            onDismissDialog = onDismissDialog,
            onSendDialog = onSendDialog,
            modifier = modifier
        )
    }
}

@Composable
fun ForgotPasswordDialogContent(
    email: String,
    onEmailChange: (String) -> Unit,
    onDismissDialog: () -> Unit,
    onSendDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 312.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
        ) {
            Text(
                text = stringResource(R.string.label_reset_password),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Theme.custom.primaryText
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.label_subtitle_reset_password),
                style = MaterialTheme.typography.bodyLarge,
                color = Theme.custom.secondaryText
            )

            Spacer(Modifier.height(16.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(coreR.string.label_email),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                AppTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    placeholder = stringResource(R.string.placeholder_email),
                    imeAction = ImeAction.Next
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                AppOutlinedButton(
                    text = stringResource(coreR.string.btn_label_cancel),
                    onClick = onDismissDialog,
                    borderColor = Theme.custom.hint,
                    contentColor = Theme.custom.hint,
                    modifier = Modifier.weight(1f)
                )

                AppPrimaryButton(
                    text = stringResource(R.string.btn_label_send_link),
                    onClick = {
                        onSendDialog()
                        onDismissDialog()
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun ForgotPasswordDialogPreview() {
    InventoryTheme {
        ForgotPasswordDialogContent(
            email = "",
            onEmailChange = {},
            onSendDialog = {},
            onDismissDialog = {}
        )
    }
}