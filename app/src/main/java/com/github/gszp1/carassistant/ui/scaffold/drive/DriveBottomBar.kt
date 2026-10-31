package com.github.gszp1.carassistant.ui.scaffold.drive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    trackLabel: String = "Track title",
    onPrevious: () -> Unit = {},
    onPlayPause: () -> Unit = {},
    onNext: () -> Unit = {},
    onOpenVolume: () -> Unit = {},
) {
    var playing by rememberSaveable { mutableStateOf(false) }

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
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DriveBarButton(
                    text = "Previous",
                    onClick = onPrevious,
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.SkipPrevious
                )
                DriveBarButton(
                    text = "Play",
                    onClick = {
                        playing = !playing
                        onPlayPause()
                    },
                    modifier = Modifier.weight(1f),
                    icon = if (playing) Icons.Default.Pause else Icons.Default.PlayArrow
                )
                DriveBarButton(
                    text = "Next",
                    onClick = onNext,
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.SkipNext
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.BottomCenter,
            ) {
                DriveBarButton(
                    text = "Volume",
                    onClick = onOpenVolume,
                    icon = Icons.AutoMirrored.Filled.VolumeUp
                )
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
