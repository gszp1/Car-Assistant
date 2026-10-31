package com.github.gszp1.carassistant.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

private fun DrivePalette.toColorScheme(): ColorScheme {
    val scheme = if (mode == DriveMode.NIGHT) darkColorScheme() else lightColorScheme()
    return scheme.copy(
        primary = barCenter,
        onPrimary = onAccent,
        primaryContainer = barMid,
        onPrimaryContainer = barCenter,
        secondary = barOutline,
        onSecondary = onAccent,
        tertiary = barLabel,
        onTertiary = onAccent,
        background = contentMid,
        onBackground = barLabel,
        surface = contentCenter,
        onSurface = barLabel,
        surfaceVariant = contentEdge,
        onSurfaceVariant = barLabel,
        outline = barOutline,
        outlineVariant = gridLine,
    )
}

@Composable
fun CarAssistantTheme(
    style: Styles = Styles.AQUAMARINE,
    mode: DriveMode = systemDriveMode(),
    content: @Composable () -> Unit
) {
    val palette = style.palette(mode)
    val colorScheme = remember(palette) { palette.toColorScheme() }

    CompositionLocalProvider(LocalDrivePalette provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

@Composable
fun systemDriveMode(): DriveMode =
    if (isSystemInDarkTheme()) DriveMode.NIGHT else DriveMode.DAY
