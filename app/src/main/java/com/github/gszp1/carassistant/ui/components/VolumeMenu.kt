package com.github.gszp1.carassistant.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeDown
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.github.gszp1.carassistant.ui.theme.CarAssistantTheme
import com.github.gszp1.carassistant.ui.theme.DrivePalette
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette
import com.github.gszp1.carassistant.ui.theme.Styles
import kotlin.math.roundToInt

private const val PercentPerLevel = 100
private const val HalfVolume = 0.5f
private val VolumeOrigin = TransformOrigin(0.5f, 1f)

internal val VolumePanelWidth = 300.dp
internal val VolumePanelHeight = 108.dp

private val MuteIconSize = 28.dp
private val RowSpacing = 8.dp

private const val MutedLabel = "Muted"
private const val MuteDescription = "Mute"
private const val UnmuteDescription = "Unmute"

@Composable
fun VolumeMenu(
    visible: Boolean,
    level: Float,
    muted: Boolean,
    onDismiss: () -> Unit,
    onLevelChange: (Float) -> Unit,
    onToggleMute: () -> Unit,
    anchorOffset: IntOffset = IntOffset.Zero,
    palette: DrivePalette = LocalDrivePalette.current,
) {
    DrivePopup(
        visible = visible,
        onDismiss = onDismiss,
        label = "VolumeMenu",
        alignment = Alignment.TopCenter,
        anchorOffset = anchorOffset,
        transformOrigin = VolumeOrigin,
    ) {
        VolumePanel(
            level = level,
            muted = muted,
            onLevelChange = onLevelChange,
            onToggleMute = onToggleMute,
            palette = palette,
        )
    }
}

@Composable
private fun VolumePanel(
    level: Float,
    muted: Boolean,
    onLevelChange: (Float) -> Unit,
    onToggleMute: () -> Unit,
    modifier: Modifier = Modifier,
    palette: DrivePalette = LocalDrivePalette.current,
) {
    var dragged by remember { mutableStateOf<Float?>(null) }

    DrivePanel(modifier = modifier.width(VolumePanelWidth), palette = palette) {
        Column(
            modifier = Modifier.padding(PanelPadding),
            verticalArrangement = Arrangement.spacedBy(RowSpacing),
        ) {
            Text(
                text = if (muted) {
                    MutedLabel
                } else {
                    "Volume ${((dragged ?: level) * PercentPerLevel).roundToInt()}%"
                },
                color = palette.barLabel,
                style = MaterialTheme.typography.labelLarge,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(RowSpacing),
            ) {
                MuteButton(
                    muted = muted,
                    level = dragged ?: level,
                    onClick = onToggleMute,
                    palette = palette,
                )
                Slider(
                    value = dragged ?: level,
                    onValueChange = {
                        dragged = it
                        onLevelChange(it)
                    },
                    onValueChangeFinished = { dragged = null },
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = palette.iconAccent,
                        activeTrackColor = palette.barCenter,
                        inactiveTrackColor = palette.barOutline,
                    ),
                )
            }
        }
    }
}

@Composable
private fun MuteButton(
    muted: Boolean,
    level: Float,
    onClick: () -> Unit,
    palette: DrivePalette,
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = volumeIcon(muted, level),
            contentDescription = if (muted) UnmuteDescription else MuteDescription,
            modifier = Modifier.size(MuteIconSize),
            tint = palette.iconAccent,
        )
    }
}

internal fun volumeIcon(muted: Boolean, level: Float): ImageVector = when {
    muted || level <= 0f -> Icons.AutoMirrored.Filled.VolumeOff
    level < HalfVolume -> Icons.AutoMirrored.Filled.VolumeDown
    else -> Icons.AutoMirrored.Filled.VolumeUp
}

@Preview(
    name = "Volume menu – night",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Preview(
    name = "Volume menu – day",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun VolumeMenuPreview() {
    CarAssistantTheme(style = Styles.AQUAMARINE) {
        VolumePanel(
            level = 0.45f,
            muted = false,
            onLevelChange = {},
            onToggleMute = {},
        )
    }
}
