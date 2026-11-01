package com.github.gszp1.carassistant.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.gszp1.carassistant.ui.theme.DrivePalette
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette

private const val VisibleTiles = 3
private const val AppTileHeightFraction = 0.82f
private val AppTileSpacing = 20.dp

val DriveApps = listOf("Phone", "Navigation", "Music")

@Composable
fun AppLauncherRow(
    modifier: Modifier = Modifier,
    apps: List<String> = DriveApps,
    onLaunch: (String) -> Unit = {},
) {
    BoxWithConstraints(modifier.fillMaxSize()) {
        val tileWidth = (maxWidth - AppTileSpacing * (VisibleTiles + 1)) / VisibleTiles
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    palette: DrivePalette = LocalDrivePalette.current,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, palette.barOutline),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = palette.iconAccent),
    ) {
        Text(
            text = label,
            textAlign = TextAlign.Center,
            maxLines = 2,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}
