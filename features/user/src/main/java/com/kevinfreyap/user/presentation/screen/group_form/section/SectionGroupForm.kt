package com.kevinfreyap.user.presentation.screen.group_form.section

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
import com.kevinfreyap.ui.components.AppTextField
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.user.R
import com.kevinfreyap.user.domain.validation.error.GroupFormError
import com.kevinfreyap.user.domain.validation.error.GroupNameError
import com.kevinfreyap.user.domain.validation.error.GroupOptionalFieldError
import com.kevinfreyap.user.presentation.action.GroupFormAction
import com.kevinfreyap.user.presentation.state.GroupFormDetailState

@Composable
fun SectionGroupForm(
    state: GroupFormDetailState,
    errors: GroupFormError?,
    onAction: (GroupFormAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.label_group_detail),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = Theme.custom.primaryText
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            AppTextField(
                value = state.name,
                onValueChange = { name ->
                    onAction(GroupFormAction.OnNameChanged(name))
                },
                label = stringResource(R.string.label_field_group_name),
                minLines = 1,
                maxLines = 1,
                unfocusedColor = Theme.custom.hint,
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Sentences,
                isError = errors?.groupNameError != null,
                errorMessage = if (errors?.groupNameError != null) {
                    when (errors.groupNameError) {
                        GroupNameError.EMPTY -> stringResource(R.string.error_group_name_empty)
                        GroupNameError.TOO_LONG -> stringResource(R.string.error_group_name_too_long)
                        GroupNameError.CONTAINS_NEWLINE -> stringResource(R.string.error_group_name_contains_newline)
                    }
                } else null
            )

            AppTextField(
                value = state.description,
                onValueChange = { description ->
                    onAction(GroupFormAction.OnDescriptionChanged(description))
                },
                label = stringResource(R.string.label_field_group_description),
                minLines = 3,
                maxLines = 5,
                unfocusedColor = Theme.custom.hint,
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Sentences,
                isError = errors?.groupDescriptionError != null,
                errorMessage = if (errors?.groupDescriptionError != null) {
                    when (errors.groupDescriptionError) {
                        GroupOptionalFieldError.TOO_LONG -> stringResource(R.string.error_group_description_too_long)
                    }
                } else null
            )

            AppTextField(
                value = state.address,
                onValueChange = { address ->
                    onAction(GroupFormAction.OnAddressChanged(address))
                },
                label = stringResource(R.string.label_field_group_address),
                minLines = 2,
                maxLines = 3,
                unfocusedColor = Theme.custom.hint,
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences,
                isError = errors?.groupAddressError != null,
                errorMessage = if (errors?.groupAddressError != null) {
                    when (errors.groupAddressError) {
                        GroupOptionalFieldError.TOO_LONG -> stringResource(R.string.error_group_address_too_long)
                    }
                } else null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SectionGroupFormPreview() {
    InventoryTheme {
        SectionGroupForm(
            state = GroupFormDetailState(
                name = "My Inventory Group",
                description = "Group description here...",
                address = "123 Main Street"
            ),
            errors = null,
            onAction = {}
        )
    }
}