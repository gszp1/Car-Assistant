package com.github.gszp1.carassistant.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeDown
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.gszp1.carassistant.media.MediaControls
import com.github.gszp1.carassistant.ui.theme.CarAssistantTheme
import com.github.gszp1.carassistant.ui.theme.DrivePalette
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette
import com.github.gszp1.carassistant.ui.theme.Styles

private const val OverlayBarSlots = 6
private const val OverlayBarAlpha = 0.82f

private val OverlayBarWidth = 44.dp
private val OverlayAccentWidth = 2.dp
private val OverlayContentPadding = 8.dp
private val OverlayButtonHeight = 48.dp
private val OverlayIconSize = 22.dp
private val OverlayDividerPadding = 6.dp
private val OverlayDividerHeight = 1.dp
private val OverlayBarHeight =
    OverlayButtonHeight * OverlayBarSlots + OverlayContentPadding * 2 + OverlayDividerHeight

@Composable
fun MapsOverlayBar(
    onReturnToApp: () -> Unit,
    modifier: Modifier = Modifier,
    media: MediaControls = MediaControls.Inactive,
    palette: DrivePalette = LocalDrivePalette.current,
) {
    Row(modifier.height(OverlayBarHeight).width(OverlayBarWidth)) {
        Column(
            modifier = Modifier
                .width(OverlayAccentWidth)
                .fillMaxHeight()
                .background(palette.barCenter.copy(alpha = OverlayBarAlpha)),
        ) {}
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(sideBarBrush(palette))
                .padding(vertical = OverlayContentPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            OverlayBarButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back to CarAssistant",
                onClick = onReturnToApp,
                palette = palette,
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = OverlayDividerPadding),
                thickness = OverlayDividerHeight,
                color = palette.barOutline,
            )
            OverlayBarButton(
                icon = Icons.Default.SkipPrevious,
                contentDescription = "Previous track",
                onClick = media::previous,
                palette = palette,
            )
            OverlayBarButton(
                icon = if (media.status.isPlaying) {
                    Icons.Default.Pause
                } else {
                    Icons.Default.PlayArrow
                },
                contentDescription = if (media.status.isPlaying) "Pause" else "Play",
                onClick = media::playPause,
                palette = palette,
            )
            OverlayBarButton(
                icon = Icons.Default.SkipNext,
                contentDescription = "Next track",
                onClick = media::next,
                palette = palette,
            )
            OverlayBarButton(
                icon = Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = "Volume up",
                onClick = media::raiseVolume,
                palette = palette,
            )
            OverlayBarButton(
                icon = Icons.AutoMirrored.Filled.VolumeDown,
                contentDescription = "Volume down",
                onClick = media::lowerVolume,
                palette = palette,
            )
        }
    }
}

@Composable
private fun OverlayBarButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    palette: DrivePalette,
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(OverlayButtonHeight),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(OverlayIconSize),
            tint = palette.iconAccent,
        )
    }
}

private fun sideBarBrush(palette: DrivePalette): Brush = Brush.horizontalGradient(
    0.0f to palette.barCenter.copy(alpha = OverlayBarAlpha),
    0.3f to palette.barMid.copy(alpha = OverlayBarAlpha),
    1.0f to palette.barEdge.copy(alpha = OverlayBarAlpha),
)

@Preview(name = "maps-overlay-bar", widthDp = 240, heightDp = 540)
@Composable
private fun MapsOverlayBarPreview() {
    CarAssistantTheme(style = Styles.AQUAMARINE) {
        Row(
            modifier = Modifier.fillMaxHeight().fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            MapsOverlayBar(onReturnToApp = {})
        }
    }
}
