package com.kevinfreyap.product.presentation.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.NumberFormat
import java.util.Locale

object ThousandSeparatorVisualTransformation: VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Ensure that only digits
        // If string empty, just return
        val originalText = text.text.filter { it.isDigit() }
        if (originalText.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        // Format -> Convert 1000 into 1.000
        // Locale ("id", "ID") forces the use of '.' as thousands separator
        val formattedText = NumberFormat.getNumberInstance(
            Locale.Builder()
                .setLanguage("id")
                .setRegion("ID")
                .build()
        ).format(originalText.toLongOrNull() ?: 0L)

        // Map Cursor -> Translate cursor position between raw and formatted
        val offsetMapping = object : OffsetMapping {

            override fun originalToTransformed(offset: Int): Int {
                val safeOffset = offset.coerceIn(0, originalText.length)
                if (safeOffset == 0) return 0

                // Check how many dots to the left of cursor
                // Total dots in the whole string
                val totalDots = (originalText.length - 1) / 3
                // Dots located to right of current cursor position
                val dotsToRight = (originalText.length - safeOffset - 1).coerceAtLeast(0) / 3
                // Dots to left of cursor
                val dotsBefore = (totalDots - dotsToRight).coerceAtLeast(0)

                return safeOffset + dotsBefore
            }

            override fun transformedToOriginal(offset: Int): Int {
                val safeOffset = offset.coerceIn(0, formattedText.length)
                var dotsBefore = 0

                // Count how many dots the cursor is past
                for (i in 0 until safeOffset) {
                    if (formattedText[i] == '.') {
                        dotsBefore++
                    }
                }
                return safeOffset - dotsBefore
            }
        }
        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}