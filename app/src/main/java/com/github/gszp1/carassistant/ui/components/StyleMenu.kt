package com.github.gszp1.carassistant.ui.components

import android.content.res.Configuration
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.github.gszp1.carassistant.ui.theme.CarAssistantTheme
import com.github.gszp1.carassistant.ui.theme.DriveMode
import com.github.gszp1.carassistant.ui.theme.DrivePalette
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette
import com.github.gszp1.carassistant.ui.theme.Styles

private const val MenuAlpha = 0.9f
private const val EnterDurationMillis = 110
private const val ExitDurationMillis = 70
private const val CollapsedScale = 0.92f
private val MenuOrigin = TransformOrigin(0f, 0f)

private fun durationFor(shown: Boolean) =
    if (shown) EnterDurationMillis else ExitDurationMillis
private val MenuPadding = 16.dp
private val MenuCorner = 20.dp
private val OptionCorner = 12.dp
private val ColumnSpacing = 14.dp
private val OptionSpacing = 8.dp
private val SwatchSize = 44.dp
private val SwatchRing = 3.dp

@Composable
fun StyleMenu(
    visible: Boolean,
    onDismiss: () -> Unit,
    onSelectStyle: (Styles) -> Unit,
    onSelectMode: (DriveMode) -> Unit,
    anchorOffset: IntOffset = IntOffset.Zero,
    palette: DrivePalette = LocalDrivePalette.current,
    style: Styles = palette.style,
    mode: DriveMode = palette.mode,
) {
    val transition = remember { MutableTransitionState(false) }
    transition.targetState = visible

    if (transition.currentState || transition.targetState) {
        Popup(
            alignment = Alignment.TopStart,
            offset = anchorOffset,
            onDismissRequest = onDismiss,
            properties = PopupProperties(focusable = true),
        ) {
            val animation = rememberTransition(transition, label = "StyleMenu")
            val alpha by animation.animateFloat(
                transitionSpec = { tween(durationFor(targetState)) },
                label = "alpha",
            ) { shown -> if (shown) 1f else 0f }
            val scale by animation.animateFloat(
                transitionSpec = { tween(durationFor(targetState)) },
                label = "scale",
            ) { shown -> if (shown) 1f else CollapsedScale }

            StylePanel(
                style = style,
                mode = mode,
                onSelectStyle = onSelectStyle,
                onSelectMode = onSelectMode,
                modifier = Modifier.graphicsLayer {
                    this.alpha = alpha
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = MenuOrigin
                },
                palette = palette,
            )
        }
    }
}

@Composable
private fun StylePanel(
    style: Styles,
    mode: DriveMode,
    onSelectStyle: (Styles) -> Unit,
    onSelectMode: (DriveMode) -> Unit,
    modifier: Modifier = Modifier,
    palette: DrivePalette = LocalDrivePalette.current,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(MenuCorner),
        color = palette.contentCenter.copy(alpha = MenuAlpha),
        border = BorderStroke(1.dp, palette.barOutline),
    ) {
        Row(
            modifier = Modifier
                .padding(MenuPadding)
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(ColumnSpacing),
        ) {
            Column(
                modifier = Modifier.width(IntrinsicSize.Max),
                verticalArrangement = Arrangement.spacedBy(OptionSpacing),
            ) {
                DriveMode.entries.forEach { option ->
                    ModeOption(
                        mode = option,
                        selected = option == mode,
                        onClick = { onSelectMode(option) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        palette = palette,
                    )
                }
            }
            VerticalDivider(color = palette.barOutline)
            Column(
                modifier = Modifier.width(IntrinsicSize.Max),
                verticalArrangement = Arrangement.spacedBy(OptionSpacing),
            ) {
                Styles.entries.forEach { option ->
                    StyleOption(
                        style = option,
                        mode = mode,
                        selected = option == style,
                        onClick = { onSelectStyle(option) },
                        modifier = Modifier.fillMaxWidth(),
                        palette = palette,
                    )
                }
            }
        }
    }
}

@Composable
private fun ModeOption(
    mode: DriveMode,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    palette: DrivePalette = LocalDrivePalette.current,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(OptionCorner),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) palette.barCenter else palette.barOutline,
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) palette.barCenter else Color.Transparent,
            contentColor = if (selected) palette.onAccent else palette.barLabel,
        ),
    ) {
        Text(
            text = mode.label,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
private fun StyleOption(
    style: Styles,
    mode: DriveMode,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    palette: DrivePalette = LocalDrivePalette.current,
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(OptionCorner))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(SwatchSize)
                .border(
                    width = if (selected) SwatchRing else 1.dp,
                    color = if (selected) palette.barCenter else palette.barOutline,
                    shape = CircleShape,
                )
                .padding(if (selected) SwatchRing + 2.dp else 1.dp)
                .clip(CircleShape)
                .background(style.palette(mode).swatchBrush)
        )
        Text(
            text = style.label,
            color = if (selected) palette.iconAccent else palette.barLabel,
            maxLines = 1,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Preview(
    name = "Style menu – night",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Preview(
    name = "Style menu – day",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun StyleMenuPreview() {
    CarAssistantTheme(style = Styles.AQUAMARINE) {
        val palette = LocalDrivePalette.current
        StylePanel(
            style = palette.style,
            mode = palette.mode,
            onSelectStyle = {},
            onSelectMode = {},
            palette = palette,
        )
    }
}
