package com.github.gszp1.carassistant.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.gszp1.carassistant.ui.theme.CarAssistantTheme
import com.github.gszp1.carassistant.ui.theme.DrivePalette
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette
import com.github.gszp1.carassistant.ui.theme.Styles

private const val GridPanelAlpha = 0.78f
private val GridPanelOrigin = TransformOrigin(0.5f, 0f)
private val ScreenInsetVertical = 40.dp
private val ScreenInsetHorizontal = 72.dp
private val BandSpacing = 16.dp

internal const val AppGridTitle = "Applications"

@Composable
private fun Modifier.tapTarget(onClick: () -> Unit): Modifier = clickable(
    interactionSource = remember { MutableInteractionSource() },
    indication = null,
    onClick = onClick,
)

@Composable
fun AppGridMenu(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    palette: DrivePalette = LocalDrivePalette.current,
    content: @Composable () -> Unit = {},
    footer: @Composable () -> Unit = {},
) {
    DrivePopup(
        visible = visible,
        onDismiss = onDismiss,
        label = "AppGridMenu",
        modifier = Modifier.fillMaxSize(),
        alignment = Alignment.Center,
        transformOrigin = GridPanelOrigin,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .tapTarget(onDismiss)
                .padding(
                    horizontal = ScreenInsetHorizontal,
                    vertical = ScreenInsetVertical,
                ),
        ) {
            AppGridPanel(
                modifier = modifier.fillMaxSize(),
                palette = palette,
                content = content,
                footer = footer,
            )
        }
    }
}

@Composable
private fun AppGridPanel(
    modifier: Modifier = Modifier,
    palette: DrivePalette = LocalDrivePalette.current,
    content: @Composable () -> Unit = {},
    footer: @Composable () -> Unit = {},
) {
    DrivePanel(
        modifier = modifier.tapTarget {},
        palette = palette,
        alpha = GridPanelAlpha,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(PanelPadding),
            verticalArrangement = Arrangement.spacedBy(BandSpacing),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = AppGridTitle,
                    color = palette.barLabel,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                content()
            }
            Box(modifier = Modifier.fillMaxWidth()) {
                footer()
            }
        }
    }
}

@Preview(
    name = "App grid – night",
    widthDp = 960,
    heightDp = 540,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Preview(
    name = "App grid – day",
    widthDp = 960,
    heightDp = 540,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun AppGridPanelPreview() {
    CarAssistantTheme(style = Styles.AQUAMARINE) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = ScreenInsetHorizontal,
                    vertical = ScreenInsetVertical,
                ),
        ) {
            AppGridPanel(modifier = Modifier.fillMaxSize())
        }
    }
}
