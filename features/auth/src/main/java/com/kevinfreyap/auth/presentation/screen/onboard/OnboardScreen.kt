package com.kevinfreyap.auth.presentation.screen.onboard

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kevinfreyap.auth.presentation.navigation.OnboardNavigation
import com.kevinfreyap.ui.R as coreR
import com.kevinfreyap.auth.R
import com.kevinfreyap.ui.components.AppIconName
import com.kevinfreyap.ui.components.AppOutlinedButton
import com.kevinfreyap.ui.components.AppPrimaryButton
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme

@Composable
fun OnboardScreen(
    onNavigate: (OnboardNavigation) -> Unit,
    modifier: Modifier = Modifier
) {
    OnboardContent(
        onNavigate = onNavigate,
        modifier = modifier
    )
}

@Composable
fun OnboardContent(
    onNavigate: (OnboardNavigation) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold (
        topBar = {
            AppIconName(
                modifier = Modifier
                    .padding(16.dp)
            )
        },
        bottomBar = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                AppPrimaryButton(
                    onClick = {
                        onNavigate(OnboardNavigation.Register)
                    },
                    text = stringResource(R.string.btn_label_get_started),
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
                AppOutlinedButton(
                    onClick = {
                        onNavigate(OnboardNavigation.Login)
                    },
                    text = stringResource(R.string.btn_label_have_account),
                    cornerRadiusPercentage = 32,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
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
            Icon(
                painter = painterResource(coreR.drawable.app_icon),
                contentDescription = "App Icon",
                tint = Color.Unspecified,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(280.dp)
            )

            Text(
                text = stringResource(R.string.label_title_tagline),
                style = MaterialTheme.typography.displaySmall.copy(
                    fontSize = 32.sp
                ),
                fontWeight = FontWeight.Bold,
                color = Theme.custom.primaryText,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.label_subtitle_description),
                style = MaterialTheme.typography.bodyLarge,
                color = Theme.custom.secondaryText
            )
        }
    }
}

@Preview(
    device = "spec:width=1080px,height=2340px,dpi=416",
    showBackground = true,
)
@Composable
fun OnboardScreenPreview() {
    InventoryTheme {
        OnboardContent(
            onNavigate = {}
        )
    }
}

@Preview(
    device = "spec:width=1080px,height=2340px,dpi=416",
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun OnboardScreenPreview_Dark() {
    InventoryTheme {
        OnboardContent(
            onNavigate = {}
        )
    }
}