package com.kevinfreyap.ui.util

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

fun Modifier.shimmerEffect(shimmerColor: Color): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "shimmer_transition")

    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    )

    val highlightColor = shimmerColor.copy(alpha = 0.5f)

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                shimmerColor,
                highlightColor,
                shimmerColor
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned {
        size = it.size
    }
}

fun Modifier.dashedBorder(
    color: Color,
    strokeWidth: Dp = 2.dp,
    dashLength: Dp = 8.dp,
    gapLength: Dp = 8.dp,
    cornerRadius: Dp = 16.dp
) = this.drawWithContent {
    drawContent()

    val strokeWidthPx = strokeWidth.toPx()

    val dashEffect = PathEffect.dashPathEffect(
        intervals = floatArrayOf(dashLength.toPx(), gapLength.toPx()),
        phase = 0f
    )

    drawRoundRect(
        color = color,
        topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2),
        size = Size(size.width - strokeWidthPx, size.height - strokeWidthPx),
        style = Stroke(
            width = strokeWidthPx,
            pathEffect = dashEffect
        ),
        cornerRadius = CornerRadius(cornerRadius.toPx())
    )
}