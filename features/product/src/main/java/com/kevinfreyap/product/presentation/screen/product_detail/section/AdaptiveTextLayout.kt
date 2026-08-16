package com.kevinfreyap.product.presentation.screen.product_detail.section

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp

@Composable
fun AdaptiveTextLayout(
    modifier: Modifier,
    singleLineContent: @Composable () -> Unit,
    stackedContent: @Composable () -> Unit
) {
    Layout (
        content = {
            Box (
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) { singleLineContent() }
            Box (
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) { stackedContent() }
        },
        modifier = modifier
    ) { measurables, constraints ->
        val singleLineMeasurable = measurables[0]
        val stackedMeasurable = measurables[1]

        val singleLinePlaceable = singleLineMeasurable.measure(
            constraints.copy(maxWidth = Constraints.Infinity)
        )

        if (singleLinePlaceable.width <= constraints.maxWidth) {
            layout(singleLinePlaceable.width, singleLinePlaceable.height) {
                singleLinePlaceable.placeRelative(0, 0)
            }
        } else {
            val stackedPlaceable = stackedMeasurable.measure(constraints)
            layout(stackedPlaceable.width, stackedPlaceable.height) {
                stackedPlaceable.placeRelative(0, 0)
            }
        }
    }
}