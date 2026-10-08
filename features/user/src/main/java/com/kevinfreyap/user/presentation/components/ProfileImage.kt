package com.kevinfreyap.user.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kevinfreyap.ui.theme.InventoryTheme
import com.kevinfreyap.ui.theme.Theme
import com.kevinfreyap.ui.util.shimmerEffect
import com.kevinfreyap.user.R

@Composable
fun ProfileImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp
) {
    Card(
        shape = RoundedCornerShape(25),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = Theme.custom.hint
        ),
        modifier = modifier
            .size(size)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (!imageUrl.isNullOrBlank()){
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    error = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.person_24),
                                contentDescription = "Error Profile Picture",
                                tint =  Theme.custom.hint,
                                modifier = Modifier.size(size.times(0.7f))
                            )
                        }
                    }
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.person_24),
                    contentDescription = "No image available",
                    tint = Theme.custom.hint,
                    modifier = Modifier
                        .size(size.times(0.7f))
                )
            }
        }
    }
}

@Composable
fun ProfileImagePlaceholder(
    shimmerColor: Color = Theme.custom.shimmer,
    size: Dp = 64.dp
) {
    Card(
        shape = RoundedCornerShape(25),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = Theme.custom.hint
        ),
        modifier = Modifier
            .size(size)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shimmerEffect(shimmerColor)
        )
    }
}

@Preview
@Composable
fun ProfileImagePreview() {
    InventoryTheme {
        ProfileImage(
            imageUrl = ""
        )
    }
}

@Preview
@Composable
fun ProfileImagePlaceholderPreview() {
    InventoryTheme {
        ProfileImagePlaceholder()
    }
}