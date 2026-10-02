package com.kevinfreyap.auth.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.auth.R
import com.kevinfreyap.auth.domain.error.AuthEmailError
import com.kevinfreyap.auth.domain.error.AuthPasswordError
import com.kevinfreyap.auth.presentation.action.AuthAction
import com.kevinfreyap.auth.presentation.state.ScreenAuthState
import com.kevinfreyap.ui.R as coreR
import com.kevinfreyap.ui.components.AppTextField
import com.kevinfreyap.ui.theme.InventoryTheme

@Composable
fun EmailPasswordInput(
    state: ScreenAuthState,
    onAction: (AuthAction.CredentialAction) -> Unit,
    lastFieldImeAction: ImeAction,
    modifier: Modifier = Modifier,
) {
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(coreR.string.label_email),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            AppTextField(
                value = state.email,
                onValueChange = {
                    onAction(AuthAction.CredentialAction.OnEmailChanged(it))
                },
                placeholder = stringResource(R.string.placeholder_email),
                imeAction = ImeAction.Next,
                isError = state.emailError != null,
                errorMessage = if (state.emailError != null) {
                    when(state.emailError) {
                        AuthEmailError.EMPTY -> stringResource(R.string.error_email_empty)
                        AuthEmailError.TOO_LONG -> stringResource(R.string.error_email_too_long)
                        AuthEmailError.INVALID_FORMAT -> stringResource(R.string.error_email_invalid)
                    }
                } else null
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(coreR.string.label_password),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            AppTextField(
                value = state.password,
                onValueChange = {
                    onAction(AuthAction.CredentialAction.OnPasswordChanged(it))
                },
                placeholder = stringResource(R.string.placeholder_password),
                imeAction = lastFieldImeAction,
                keyboardType = KeyboardType.Password,
                visualTransformation = if (isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    if (state.password.isNotBlank()) {
                        val image = if (isPasswordVisible) {
                            painterResource(R.drawable.visibility_24)
                        } else {
                            painterResource(R.drawable.visibility_off_24)
                        }

                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                painter = image,
                                contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                            )
                        }
                    }
                },
                isError = state.passwordError != null,
                errorMessage = if (state.passwordError != null) {
                    when(state.passwordError) {
                        AuthPasswordError.EMPTY -> stringResource(R.string.error_pass_empty)
                        AuthPasswordError.TOO_SHORT -> stringResource(R.string.error_pass_too_short)
                        AuthPasswordError.TOO_LONG -> stringResource(R.string.error_pass_too_long)
                        AuthPasswordError.NO_UPPERCASE -> stringResource(R.string.error_pass_no_uppercase)
                        AuthPasswordError.NO_DIGIT -> stringResource(R.string.error_pass_no_digit)
                    }
                } else null
            )
        }


    }
}

@Preview(
    showBackground = true
)
@Composable
fun EmailPasswordInputPreview() {
    InventoryTheme {
        EmailPasswordInput(
            state = ScreenAuthState(),
            onAction = {},
            lastFieldImeAction = ImeAction.Done,
        )
    }
}