package com.github.gszp1.carassistant.ui.scaffold.drive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.gszp1.carassistant.ui.components.Clock
import com.github.gszp1.carassistant.ui.theme.DriveMode
import com.github.gszp1.carassistant.ui.theme.DrivePalette
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette

@Composable
fun DriveTopBar(
    modifier: Modifier = Modifier,
    palette: DrivePalette = LocalDrivePalette.current,
    accentWidth: Dp = 3.dp,
    onOpenAppStyle: () -> Unit = {},
    onToggleMode: () -> Unit = {},
    onOpenAppGrid: () -> Unit = {},
) {
    Box(
        modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .arcBarBackground(palette, ::topArcPath)
            .arcOutline(palette.barCenter, accentWidth, ::topArcOutlinePath)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(BarSafeContentHeight),
            ) {
                DriveBarButton(
                    text = "App style",
                    onClick = onOpenAppStyle,
                    modifier = Modifier.weight(1f),
                )
                DriveBarButton(
                    text = if (palette.mode == DriveMode.NIGHT) "Night" else "Day",
                    onClick = onToggleMode,
                    modifier = Modifier.weight(1f),
                )
            }
            Clock(Modifier.weight(1f))
            DriveBarButton(
                text = "App grid",
                onClick = onOpenAppGrid,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(BarSafeContentHeight),
            )
        }
    }
}
