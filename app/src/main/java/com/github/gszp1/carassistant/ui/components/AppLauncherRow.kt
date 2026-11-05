package com.github.gszp1.carassistant.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.gszp1.carassistant.ui.theme.DrivePalette
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette

private const val VisibleTiles = 3
private const val AppTileHeightFraction = 0.82f
private const val AppIconWidthFraction = 0.62f
private const val AppIconHeightFraction = 0.62f
private val AppTileSpacing = 20.dp
private val AppTileCornerRadius = 16.dp
private val AppIconSpacing = 10.dp

const val NavigationApp = "Navigation"

val DriveApps = listOf("Phone", NavigationApp, "Music")

private fun appIcon(app: String): ImageVector? = when (app) {
    NavigationApp -> NavigationArrow
    else -> null
}

@Composable
fun AppLauncherRow(
    modifier: Modifier = Modifier,
    apps: List<String> = DriveApps,
    onLaunch: (String) -> Unit = {},
) {
    BoxWithConstraints(modifier.fillMaxSize()) {
        val tileWidth = (maxWidth - AppTileSpacing * (VisibleTiles + 1)) / VisibleTiles
        val tileHeight = maxHeight * AppTileHeightFraction
        val iconSize = minOf(tileWidth * AppIconWidthFraction, tileHeight * AppIconHeightFraction)
        LazyRow(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(
                space = AppTileSpacing,
                alignment = Alignment.CenterHorizontally,
            ),
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = AppTileSpacing),
        ) {
            items(apps, key = { it }) { app ->
                AppTile(
                    label = app,
                    icon = appIcon(app),
                    iconSize = iconSize,
                    onClick = { onLaunch(app) },
                    modifier = Modifier
                        .width(tileWidth)
                        .fillMaxHeight(AppTileHeightFraction),
                )
            }
        }
    }
}

@Composable
private fun AppTile(
    label: String,
    iconSize: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    palette: DrivePalette = LocalDrivePalette.current,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(AppTileCornerRadius),
        color = Color.Transparent,
        contentColor = palette.iconAccent,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = AppIconSpacing,
                alignment = Alignment.CenterVertically,
            ),
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                )
            }
            Text(
                text = label,
                textAlign = TextAlign.Center,
                maxLines = 2,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}
