package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val TarkShastraColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = NavyDeepest,
    primaryContainer = GoldDark,
    onPrimaryContainer = GoldLight,
    secondary = InfoCyan,
    onSecondary = NavyDeepest,
    secondaryContainer = NavyCardElevated,
    onSecondaryContainer = TextPrimary,
    tertiary = PurpleAccent,
    onTertiary = TextPrimary,
    background = NavyBackground,
    onBackground = TextPrimary,
    surface = NavySurface,
    onSurface = TextPrimary,
    surfaceVariant = NavyCard,
    onSurfaceVariant = TextSecondary,
    outline = NavyBorder,
    error = ErrorRed,
    onError = TextPrimary
)

@Composable
fun TarkShastraTheme(
    darkTheme: Boolean = true, // We optimize for dramatic, immersive hot-seat dark palette
    content: @Composable () -> Unit
) {
    val colorScheme = TarkShastraColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = NavyDeepest.toArgb()
            window.navigationBarColor = NavyDeepest.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
