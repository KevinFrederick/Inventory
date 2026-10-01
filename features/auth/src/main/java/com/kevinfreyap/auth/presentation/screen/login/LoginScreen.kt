package com.kevinfreyap.auth.presentation.screen.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.kevinfreyap.auth.presentation.navigation.LoginNavigation
import com.kevinfreyap.ui.components.AppCenterTopBar
import com.kevinfreyap.ui.R as coreR
import com.kevinfreyap.ui.components.AppIconName
import com.kevinfreyap.ui.components.AppPrimaryButton
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun LoginScreen(
    onNavigate: (LoginNavigation) -> Unit,
    modifier: Modifier = Modifier
) {
    LoginContent(
        onNavigate = onNavigate,
        modifier = modifier
    )
}

@Composable
fun LoginContent(
    onNavigate: (LoginNavigation) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            AppCenterTopBar(
                title = "",
                onBackClick = {
                    onNavigate(LoginNavigation.NavigateUp)
                },
            )
        },
        bottomBar = {
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.label_login_to_register))
                    append(" ")
                    withStyle(style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )) {
                        append(stringResource(R.string.text_btn_create_an_account))
                    }
                },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onNavigate(LoginNavigation.Register)
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
                greeting = stringResource(R.string.label_login_greeting),
                headline = stringResource(R.string.label_login_headline),
                subtitle = stringResource(R.string.label_login_subtitle),
            )

            Spacer(Modifier.height(24.dp))

            EmailPasswordInput(
                lastFieldImeAction = ImeAction.Done
            )

            TextButton(
                onClick = {

                },
                modifier = Modifier
                    .align(
                        Alignment.End
                    )
            ) {
                Text(
                    text = stringResource(R.string.text_btn_forgot_password)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            AppPrimaryButton(
                text = stringResource(R.string.btn_label_sign_in),
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
    showBackground = true
)
@Composable
fun LoginScreenPreview() {
    InventoryTheme {
        LoginContent(
            onNavigate = {}
        )
    }
}