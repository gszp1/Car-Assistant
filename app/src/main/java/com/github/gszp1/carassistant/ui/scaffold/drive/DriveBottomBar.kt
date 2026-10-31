package com.github.gszp1.carassistant.ui.scaffold.drive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.gszp1.carassistant.ui.theme.DrivePalette
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette

private const val TransportWeight = 1.2f

@Composable
fun DriveBottomBar(
    modifier: Modifier = Modifier,
    palette: DrivePalette = LocalDrivePalette.current,
    accentWidth: Dp = 3.dp,
    trackLabel: String = "Tytuł utworu",
    onPrevious: () -> Unit = {},
    onPlayPause: () -> Unit = {},
    onNext: () -> Unit = {},
    onOpenVolume: () -> Unit = {},
) {
    Box(
        modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .arcBarBackground(palette, ::bottomArcPath)
            .arcOutline(palette.barCenter, accentWidth, ::bottomArcOutlinePath)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
        ) {
            DriveBarLabelText(
                text = trackLabel,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(BarSafeContentHeight),
                color = palette.barLabel,
            )
            Row(
                modifier = Modifier
                    .weight(TransportWeight)
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DriveBarButton(text = "Poprzedni", onClick = onPrevious)
                DriveBarButton(text = "Odtwórz", onClick = onPlayPause)
                DriveBarButton(text = "Następny", onClick = onNext)
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.BottomCenter,
            ) {
                DriveBarButton(text = "Głośność", onClick = onOpenVolume)
            }
        }
    }
}

@Composable
private fun DriveBarLabelText(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Text(
            text = text,
            color = color,
            textAlign = TextAlign.Center,
            maxLines = 2,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}
