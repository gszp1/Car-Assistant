package com.github.gszp1.carassistant.ui.scaffold.drive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.github.gszp1.carassistant.ui.components.Clock
import com.github.gszp1.carassistant.ui.components.StyleMenu
import com.github.gszp1.carassistant.ui.theme.DriveMode
import com.github.gszp1.carassistant.ui.theme.DrivePalette
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette
import com.github.gszp1.carassistant.ui.theme.Styles

private val MenuGap = 4.dp
private val MenuInset = 12.dp

@Composable
fun DriveTopBar(
    modifier: Modifier = Modifier,
    palette: DrivePalette = LocalDrivePalette.current,
    accentWidth: Dp = 3.dp,
    onSelectStyle: (Styles) -> Unit = {},
    onSelectMode: (DriveMode) -> Unit = {},
    onOpenAppGrid: () -> Unit = {},
) {
    var stylesVisible by rememberSaveable { mutableStateOf(false) }
    var anchorHeight by remember { mutableIntStateOf(0) }
    val menuOffset = with(LocalDensity.current) {
        IntOffset(MenuInset.roundToPx(), MenuGap.roundToPx())
    }

    Box(
        modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .arcBarBackground(palette, ::topArcPath)
            .arcOutline(palette.barCenter, accentWidth, ::topArcOutlinePath)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(BarSafeContentHeight)
                    .onSizeChanged { anchorHeight = it.height },
            ) {
                DriveBarButton(
                    text = "Style",
                    onClick = { stylesVisible = !stylesVisible },
                    modifier = Modifier.fillMaxSize(),
                    icon = Icons.Default.Palette,
                )
                StyleMenu(
                    visible = stylesVisible,
                    onDismiss = { stylesVisible = false },
                    onSelectStyle = onSelectStyle,
                    onSelectMode = onSelectMode,
                    anchorOffset = menuOffset.copy(y = anchorHeight + menuOffset.y),
                    palette = palette,
                )
            }
            Clock(Modifier.weight(1f))
            DriveBarButton(
                text = "App grid",
                onClick = onOpenAppGrid,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(BarSafeContentHeight),
            )
        }
    }
}
