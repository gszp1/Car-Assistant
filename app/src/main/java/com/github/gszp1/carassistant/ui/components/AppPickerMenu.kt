package com.github.gszp1.carassistant.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.gszp1.carassistant.launcher.GridApp
import com.github.gszp1.carassistant.ui.theme.DrivePalette
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette

private const val PickerPanelAlpha = 0.92f

private const val PickerWidthFraction = 0.6f

private val PickerPanelOrigin = TransformOrigin(0.5f, 0.5f)

private val ScreenInsetVertical = 48.dp

private val RowSpacing = 8.dp

private val RowPadding = 12.dp

private val RowCorner = 12.dp

private val RowIconSize = 40.dp

private val RowIconSpacing = 16.dp

internal const val AppPickerTitle = "Add application"

internal const val NoAppsToAddMessage = "Every installed application is already on the grid"

@Composable
fun AppPickerMenu(
    visible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    palette: DrivePalette = LocalDrivePalette.current,
    apps: List<GridApp> = emptyList(),
    onPickApp: (GridApp) -> Unit = {},
) {
    DrivePopup(
        visible = visible,
        onDismiss = onDismiss,
        label = "AppPickerMenu",
        modifier = Modifier.fillMaxSize(),
        alignment = Alignment.Center,
        transformOrigin = PickerPanelOrigin,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .dismissOnTap(onDismiss)
                .padding(vertical = ScreenInsetVertical),
            contentAlignment = Alignment.Center,
        ) {
            DrivePanel(
                modifier = modifier
                    .fillMaxWidth(PickerWidthFraction)
                    .fillMaxHeight()
                    .dismissOnTap {},
                palette = palette,
                alpha = PickerPanelAlpha,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(PanelPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(RowSpacing),
                ) {
                    Text(
                        text = AppPickerTitle,
                        color = palette.barLabel,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    if (apps.isEmpty()) {
                        Text(
                            text = NoAppsToAddMessage,
                            color = palette.barLabel,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(RowSpacing),
                        ) {
                            items(apps, key = GridApp::packageName) { app ->
                                AppPickerRow(
                                    app = app,
                                    onClick = { onPickApp(app) },
                                    palette = palette,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AppPickerRow(
    app: GridApp,
    onClick: () -> Unit,
    palette: DrivePalette = LocalDrivePalette.current,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RowCorner),
        color = Color.Transparent,
        contentColor = palette.iconAccent,
    ) {
        Row(
            modifier = Modifier.padding(RowPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(RowIconSpacing),
        ) {
            AppIcon(packageName = app.packageName, modifier = Modifier.size(RowIconSize))
            Text(
                text = app.label,
                color = palette.barLabel,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}
