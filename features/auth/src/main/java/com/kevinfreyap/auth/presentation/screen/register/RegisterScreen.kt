package com.kevinfreyap.auth.presentation.screen.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.auth.R
import com.kevinfreyap.auth.presentation.components.AuthHeaderText
import com.kevinfreyap.auth.presentation.components.EmailPasswordInput
import com.kevinfreyap.auth.presentation.components.GoogleButton
import com.kevinfreyap.auth.presentation.components.HorizontalDividerOr
import com.kevinfreyap.auth.presentation.navigation.RegisterNavigation
import com.kevinfreyap.ui.components.AppCenterTopBar
import com.kevinfreyap.ui.R as coreR
import com.kevinfreyap.ui.components.AppIconName
import com.kevinfreyap.ui.components.AppPrimaryButton
import com.kevinfreyap.ui.components.AppTextField
import com.kevinfreyap.ui.theme.InventoryTheme

@Composable
fun RegisterScreen(
    onNavigate: (RegisterNavigation) -> Unit,
    modifier: Modifier = Modifier
) {
    RegisterContent(
        onNavigate = onNavigate,
        modifier = modifier
    )
}

@Composable
fun RegisterContent(
    onNavigate: (RegisterNavigation) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            AppCenterTopBar(
                title = "",
                onBackClick = {
                    onNavigate(RegisterNavigation.NavigateUp)
                },
            )
        },
        bottomBar = {
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.label_register_to_login))
                    append(" ")
                    withStyle(style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )) {
                        append(stringResource(R.string.text_btn_sign_in))
                    }
                },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onNavigate(RegisterNavigation.Login)
                    }
                    .padding(24.dp)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 16.dp
                )
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            AuthHeaderText(
                greeting = stringResource(R.string.label_register_greeting),
                headline = stringResource(R.string.label_register_headline),
                subtitle = stringResource(R.string.label_register_subtitle),
            )

            Spacer(Modifier.height(24.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(coreR.string.label_name),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                AppTextField(
                    value = "",
                    onValueChange = {  },
                    placeholder = stringResource(R.string.placeholder_name),
                    imeAction = ImeAction.Next
                )
            }

            EmailPasswordInput(
                lastFieldImeAction = ImeAction.Next,
                modifier = Modifier
                    .padding(vertical = 16.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.label_register_confirm_password),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                AppTextField(
                    value = "",
                    onValueChange = {  },
                    placeholder = stringResource(R.string.placeholder_confirm_password),
                    imeAction = ImeAction.Next
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            AppPrimaryButton(
                text = stringResource(R.string.btn_label_create_account),
                onClick = {  },
                cornerRadiusPercentage = 32,
                textAlign = TextAlign.Start,
                trailingIcon = {
                    Icon(
                        painter = painterResource(coreR.drawable.arrow_forward_24),
                        contentDescription = null,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            HorizontalDividerOr()

            Spacer(modifier = Modifier.height(24.dp))

            GoogleButton(
                onClick = {

                },
                cornerRadius = 32,
                modifier = Modifier
                    .height(56.dp)
            )
        }
    }
}

@Preview(
    showBackground = true,
    device = "spec:width=1080px,height=2340px,dpi=416",
)
@Composable
fun RegisterScreenPreview() {
    InventoryTheme {
        RegisterContent(
            onNavigate = {}
        )
    }
}