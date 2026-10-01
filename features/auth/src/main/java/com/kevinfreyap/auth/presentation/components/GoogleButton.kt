package com.kevinfreyap.auth.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kevinfreyap.auth.R
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.R as coreR
import com.kevinfreyap.ui.theme.Theme

@Composable
fun GoogleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Int = 50
) {
    OutlinedButton(
        onClick = onClick,
        border = BorderStroke(
            1.dp,
            Theme.custom.primaryText
        ),
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Theme.custom.primaryText
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(coreR.drawable.google_icon),
            contentDescription = "Google",
            tint = null
        )

        Spacer(
            modifier = Modifier
                .width(8.dp)
        )

        Text(
            text = stringResource(R.string.btn_label_continue_with_google),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun GoogleButtonPreview() {
    InventoryTheme {
        GoogleButton(
            onClick = {}
        )
    }
}