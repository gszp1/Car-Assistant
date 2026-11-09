package com.github.gszp1.carassistant.ui.scaffold.drive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.github.gszp1.carassistant.media.MediaControls
import com.github.gszp1.carassistant.media.MediaStatus
import com.github.gszp1.carassistant.ui.components.VolumeMenu
import com.github.gszp1.carassistant.ui.components.VolumePanelHeight
import com.github.gszp1.carassistant.ui.components.volumeIcon
import com.github.gszp1.carassistant.ui.theme.DrivePalette
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette

private const val TransportWeight = 1.2f
private const val LabelSeparator = " • "

private val VolumeMenuGap = 8.dp

private const val NoAccessLabel = "Tap play to allow media control"
private const val NoSessionLabel = "Nothing playing"
private const val UnknownTrackLabel = "Playing"

@Composable
fun DriveBottomBar(
    modifier: Modifier = Modifier,
    palette: DrivePalette = LocalDrivePalette.current,
    accentWidth: Dp = 3.dp,
    media: MediaControls = MediaControls.Inactive,
) {
    val status = media.status
    var volumeVisible by rememberSaveable { mutableStateOf(false) }
    val volumeOffset = with(LocalDensity.current) {
        IntOffset(0, -(VolumePanelHeight + VolumeMenuGap).roundToPx())
    }

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
                text = status.trackLabel(),
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
                    onClick = media::previous,
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.SkipPrevious
                )
                DriveBarButton(
                    text = if (status.isPlaying) "Pause" else "Play",
                    onClick = media::playPause,
                    modifier = Modifier.weight(1f),
                    icon = if (status.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow
                )
                DriveBarButton(
                    text = "Next",
                    onClick = media::next,
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
                    text = if (status.isMuted) "Muted" else "Volume",
                    onClick = { volumeVisible = !volumeVisible },
                    icon = volumeIcon(status.isMuted, status.volumeLevel)
                )
                VolumeMenu(
                    visible = volumeVisible,
                    level = status.volumeLevel,
                    muted = status.isMuted,
                    onDismiss = { volumeVisible = false },
                    onLevelChange = media::setVolumeLevel,
                    onToggleMute = media::toggleMute,
                    anchorOffset = volumeOffset,
                    palette = palette,
                )
            }
        }
    }
}

private fun MediaStatus.trackLabel(): String = when {
    !hasAccess -> NoAccessLabel
    !hasSession -> NoSessionLabel
    else -> listOfNotNull(title, artist)
        .joinToString(LabelSeparator)
        .ifBlank { UnknownTrackLabel }
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
