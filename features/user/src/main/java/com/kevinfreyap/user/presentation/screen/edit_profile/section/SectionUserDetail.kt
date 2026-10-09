package com.kevinfreyap.user.presentation.screen.edit_profile.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.ui.components.AppSymbolTextField
import com.kevinfreyap.ui.components.AppTextField
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.user.R
import com.kevinfreyap.user.domain.validation.error.UserFormError
import com.kevinfreyap.user.domain.validation.error.UserNameError
import com.kevinfreyap.user.domain.validation.error.UserOptionalFieldError
import com.kevinfreyap.user.domain.validation.error.UserPhoneNumberError
import com.kevinfreyap.user.presentation.action.EditProfileAction
import com.kevinfreyap.user.presentation.state.UserDetailState

@Composable
fun SectionUserDetail(
    state: UserDetailState,
    errors: UserFormError?,
    onAction: (EditProfileAction.UserDetailAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.label_profile_detail),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Theme.custom.primaryText
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AppTextField(
                value = state.email,
                onValueChange = {},
                label = stringResource(R.string.label_field_email),
                minLines = 1,
                maxLines = 1,
                readOnly = true,
                enabled = false,
                disabledTextColor = Theme.custom.hint
            )

            AppTextField(
                value = state.name,
                onValueChange = { name ->
                    onAction(EditProfileAction.UserDetailAction.OnNameChanged(name))
                },
                label = stringResource(R.string.label_field_name),
                minLines = 1,
                maxLines = 1,
                unfocusedColor = Theme.custom.hint,
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Sentences,
                isError = errors?.nameError != null,
                errorMessage = if (errors?.nameError != null) {
                    when(errors.nameError) {
                        UserNameError.EMPTY -> stringResource(R.string.error_user_name_empty)
                        UserNameError.TOO_LONG -> stringResource(R.string.error_user_name_too_long)
                        UserNameError.CONTAINS_NEWLINE -> stringResource(R.string.error_user_name_contains_newline)
                    }
                } else null
            )

            AppSymbolTextField(
                value = state.phoneNumber ?: "",
                placeholder = stringResource(R.string.label_field_phone_number),
                symbol = stringResource(R.string.label_phone_code),
                onValueChange = { phoneNumber ->
                    onAction(EditProfileAction.UserDetailAction.OnPhoneNumberChanged(phoneNumber))
                },
                isError = errors?.phoneNumberError != null,
                errorMessage = if (errors?.phoneNumberError != null) {
                    when(errors.phoneNumberError) {
                        UserPhoneNumberError.TOO_LONG -> stringResource(R.string.error_user_phone_too_long)
                        UserPhoneNumberError.INVALID_FORMAT -> stringResource(R.string.error_user_phone_invalid)
                    }
                } else null
            )

            AppTextField(
                value = state.jobTitle ?: "",
                onValueChange = { jobTitle ->
                    onAction(EditProfileAction.UserDetailAction.OnJobTitleChanged(jobTitle))
                },
                label = stringResource(R.string.label_field_job_title),
                minLines = 1,
                maxLines = 1,
                unfocusedColor = Theme.custom.hint,
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences,
                isError = errors?.jobTitleError != null,
                errorMessage = if (errors?.jobTitleError != null) {
                    when(errors.jobTitleError) {
                        UserOptionalFieldError.TOO_LONG -> stringResource(R.string.error_user_job_title_too_long)
                        UserOptionalFieldError.CONTAINS_NEWLINE -> stringResource(R.string.error_user_job_title_contains_newline)
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
fun SectionUserDetailPreview() {
    InventoryTheme {
        SectionUserDetail(
            state = UserDetailState(
                name = "User 1",
                email = "user@email.com",
                profileUrl = null,
                phoneNumber = null,
                jobTitle = null
            ),
            errors = UserFormError(

            ),
            onAction = {}
        )
    }
}
