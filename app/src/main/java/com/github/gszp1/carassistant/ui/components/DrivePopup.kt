package com.github.gszp1.carassistant.ui.components

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.github.gszp1.carassistant.ui.theme.DrivePalette
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette

private const val PanelAlpha = 0.9f
private const val EnterDurationMillis = 110
private const val ExitDurationMillis = 70
private const val CollapsedScale = 0.92f

internal val PanelCorner = 20.dp
internal val PanelPadding = 16.dp

private fun durationFor(shown: Boolean) =
    if (shown) EnterDurationMillis else ExitDurationMillis

@Composable
internal fun DrivePopup(
    visible: Boolean,
    onDismiss: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopStart,
    anchorOffset: IntOffset = IntOffset.Zero,
    transformOrigin: TransformOrigin = TransformOrigin(0f, 0f),
    content: @Composable () -> Unit,
) {
    val transition = remember { MutableTransitionState(false) }
    transition.targetState = visible
    if (!transition.currentState && !transition.targetState) return

    Popup(
        alignment = alignment,
        offset = anchorOffset,
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true),
    ) {
        val animation = rememberTransition(transition, label = label)
        val alpha by animation.animateFloat(
            transitionSpec = { tween(durationFor(targetState)) },
            label = "alpha",
        ) { shown -> if (shown) 1f else 0f }
        val scale by animation.animateFloat(
            transitionSpec = { tween(durationFor(targetState)) },
            label = "scale",
        ) { shown -> if (shown) 1f else CollapsedScale }

        Box(
            modifier = modifier.graphicsLayer {
                this.alpha = alpha
                scaleX = scale
                scaleY = scale
                this.transformOrigin = transformOrigin
            },
        ) {
            content()
        }
    }
}

@Composable
internal fun DrivePanel(
    modifier: Modifier = Modifier,
    palette: DrivePalette = LocalDrivePalette.current,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(PanelCorner),
        color = palette.contentCenter.copy(alpha = PanelAlpha),
        border = BorderStroke(1.dp, palette.barOutline),
        content = content,
    )
}
