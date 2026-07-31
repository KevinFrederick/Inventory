package com.kevinfreyap.ui.theme

import android.app.Activity
import androidx.compose.ui.graphics.Color
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    background = DarkGray300,

    primary = Primary300,
    onPrimary = Primary900,

    secondary = Primary200,
    onSecondary = Primary800,

    surface = DarkGray200,
    surfaceVariant = DarkGray100,
    onSurfaceVariant = Gray400,

    secondaryContainer = Primary300.copy(alpha = 0.1f),
    onSecondaryContainer = Primary300,

    surfaceContainer = DarkGray250,

    error = Red700,
    onError = White
)

private val LightColorScheme = lightColorScheme(
    background = Gray100,

    primary = Primary500,
    onPrimary = White,

    secondary = Primary900,
    onSecondary = White,

    surface = White,
    surfaceVariant = Gray200,
    onSurfaceVariant = Gray900,

    secondaryContainer = Primary500.copy(alpha = 0.1f),
    onSecondaryContainer = Primary500,

    surfaceContainer = Primary100.copy(alpha = 0.7f),

    error = Red500,
    onError = White
)

@Immutable
data class ExtendedColors(
    val primaryText: Color,
    val secondaryText: Color,
    val success: Color,
    val warning: Color,
    val shimmer: Color,
    val hint: Color
)

val LightExtendedColors = ExtendedColors(
    primaryText = DarkGray400,
    secondaryText = Gray800,
    success = Green500,
    warning = Orange500,
    shimmer = Gray300,
    hint = Gray700
)

val DarkExtendedColors = ExtendedColors(
    primaryText = Gray50,
    secondaryText = Gray500,
    success = Green700,
    warning = Orange700,
    shimmer = DarkGray100,
    hint = Gray600
)

// Fallback
val LocalExtendedColors = staticCompositionLocalOf {
    LightExtendedColors
}

@Composable
fun InventoryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val customColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            @Suppress("DEPRECATION")
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalExtendedColors provides customColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            content = content
        )
    }
}

object Theme {
    val custom: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}