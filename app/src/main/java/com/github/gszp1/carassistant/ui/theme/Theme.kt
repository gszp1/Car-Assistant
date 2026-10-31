package com.github.gszp1.carassistant.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

private fun DrivePalette.toColorScheme() = darkColorScheme(
    primary = barCenter,
    onPrimary = DriveOnAccent,
    primaryContainer = barMid,
    onPrimaryContainer = barCenter,
    secondary = barOutline,
    onSecondary = barCenter,
    tertiary = barLabel,
    onTertiary = DriveOnAccent,
    background = contentMid,
    onBackground = barLabel,
    surface = contentCenter,
    onSurface = barLabel,
    surfaceVariant = contentEdge,
    onSurfaceVariant = barLabel,
    outline = barOutline,
    outlineVariant = gridLine,
)

@Composable
fun CarAssistantTheme(
    style: Styles = Styles.AQUAMARINE,
    content: @Composable () -> Unit
) {
    val palette = style.palette
    val colorScheme = remember(palette) { palette.toColorScheme() }

    CompositionLocalProvider(LocalDrivePalette provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
