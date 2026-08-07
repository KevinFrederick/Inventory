package com.kevinfreyap.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kevinfreyap.ui.R
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.ui.util.dashedBorder
import com.kevinfreyap.ui.util.debouncedClick

@Composable
fun AppImageUpload(
    icon: Painter,
    label: String,
    uriString: String?,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
) {
    val debouncedClick = debouncedClick(onClick = onClick)
    val debouncedRemove = debouncedClick(onClick = onRemove)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .padding(12.dp)
        ) {
            Card(
                onClick = debouncedClick,
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = Theme.custom.hint
                ),
                modifier = Modifier
                    .size(160.dp)
                    .then(
                        if (uriString.isNullOrBlank()) {
                            Modifier.dashedBorder(
                                color = if (isError) MaterialTheme.colorScheme.error else Theme.custom.hint,
                                strokeWidth = 2.dp,
                                cornerRadius = 16.dp
                            )
                        } else {
                            Modifier
                        }
                    )

            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (!uriString.isNullOrBlank()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(uriString)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Selected Product Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    } else {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Icon(
                                painter = icon,
                                contentDescription = label,
                                tint = if (isError) MaterialTheme.colorScheme.error else Theme.custom.hint,
                                modifier = Modifier
                                    .size(48.dp)
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isError) MaterialTheme.colorScheme.error else Theme.custom.hint,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            if (!uriString.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(
                            x = 12.dp,
                            y = 12.dp
                        )
                        .shadow(
                            elevation = 4.dp,
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable(
                            onClick = debouncedClick
                        )
                        .padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.edit_24),
                        contentDescription = "Change Image",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(
                            x = 12.dp,
                            y = (-12).dp
                        )
                        .shadow(
                            elevation = 4.dp,
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable(
                            onClick = debouncedRemove
                        )
                        .padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.delete_24),
                        contentDescription = "Remove Image",
                        tint = Theme.custom.hint,
                    )
                }
            }
        }

        errorMessage?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .widthIn(max = 160.dp)
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
fun AppFileUploadPreview() {
    InventoryTheme {
        AppImageUpload(
            icon = painterResource(R.drawable.close_24),
            label = "Upload Image\n(optional)",
            uriString = "",
            onClick = {},
            onRemove = {}
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun AppFileUploadPreview_Filled() {
    InventoryTheme {
        AppImageUpload(
            icon = painterResource(R.drawable.close_24),
            label = "Upload Image\n(optional)",
            uriString = "test",
            onClick = {},
            onRemove = {}
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun AppFileUploadPreview_Error() {
    InventoryTheme {
        AppImageUpload(
            icon = painterResource(R.drawable.close_24),
            label = "Upload Image\n(optional)",
            uriString = "",
            isError = true,
            errorMessage = "Something Wrong",
            onClick = {},
            onRemove = {}
        )
    }
}