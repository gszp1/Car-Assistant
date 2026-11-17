package com.github.gszp1.carassistant.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
private val TileSpacing = 16.dp
private val TileCorner = 16.dp
private val TileBorder = 2.dp
private val MinTileSize = 32.dp
private const val AddIconFraction = 0.4f
private const val VisibleRows = 2

internal const val AddAppDescription = "Add application"

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
    tiles: Int = 0,
    onAddApp: () -> Unit = {},
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
                tiles = tiles,
                onAddApp = onAddApp,
                footer = footer,
            )
        }
    }
}

@Composable
private fun AppGridPanel(
    modifier: Modifier = Modifier,
    palette: DrivePalette = LocalDrivePalette.current,
    tiles: Int = 0,
    onAddApp: () -> Unit = {},
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
            AppTileGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                tiles = tiles,
                onAddApp = onAddApp,
                palette = palette,
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                footer()
            }
        }
    }
}

@Composable
private fun AppTileGrid(
    modifier: Modifier = Modifier,
    tiles: Int = 0,
    onAddApp: () -> Unit = {},
    palette: DrivePalette = LocalDrivePalette.current,
) {
    BoxWithConstraints(modifier) {
        val tileSize = tileSizeFor(maxHeight)
        val columns = columnsFor(maxWidth, tileSize)
        val rows = ((tiles + 1) + columns - 1) / columns
        VerticalPager(
            state = rememberPagerState { rows },
            modifier = Modifier
                .width(tileSize * columns + TileSpacing * (columns - 1))
                .fillMaxHeight()
                .align(Alignment.Center),
            pageSize = PageSize.Fixed(tileSize),
            pageSpacing = TileSpacing,
        ) { row ->
            TileRow(
                first = row * columns,
                last = minOf((row + 1) * columns, tiles + 1) - 1,
                addTileIndex = tiles,
                tileSize = tileSize,
                onAddApp = onAddApp,
                palette = palette,
            )
        }
    }
}

private fun tileSizeFor(available: Dp): Dp =
    ((available - TileSpacing * (VisibleRows - 1)) / VisibleRows).coerceAtLeast(MinTileSize)

private fun columnsFor(available: Dp, tileSize: Dp): Int =
    ((available + TileSpacing) / (tileSize + TileSpacing)).toInt().coerceAtLeast(1)

@Composable
private fun TileRow(
    first: Int,
    last: Int,
    addTileIndex: Int,
    tileSize: Dp,
    onAddApp: () -> Unit,
    palette: DrivePalette = LocalDrivePalette.current,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(TileSpacing),
    ) {
        (first..last).forEach { index ->
            if (index == addTileIndex) {
                AddTile(onClick = onAddApp, tileSize = tileSize, palette = palette)
            } else {
                EmptyTile(tileSize = tileSize, palette = palette)
            }
        }
    }
}

@Composable
private fun EmptyTile(
    tileSize: Dp,
    palette: DrivePalette = LocalDrivePalette.current,
) {
    Box(
        modifier = Modifier
            .size(tileSize)
            .border(TileBorder, palette.barOutline, RoundedCornerShape(TileCorner)),
    )
}

@Composable
private fun AddTile(
    onClick: () -> Unit,
    tileSize: Dp,
    palette: DrivePalette = LocalDrivePalette.current,
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.size(tileSize),
        shape = RoundedCornerShape(TileCorner),
        color = Color.Transparent,
        contentColor = palette.iconAccent,
        border = BorderStroke(TileBorder, palette.barCenter),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = AddAppDescription,
                modifier = Modifier.size(tileSize * AddIconFraction),
            )
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
