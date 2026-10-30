package com.github.gszp1.carassistant.ui.scaffold.drive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.gszp1.carassistant.ui.theme.DriveBarBrush
import com.github.gszp1.carassistant.ui.theme.DriveBarOutline

@Composable
fun DriveBottomBar(
    modifier: Modifier = Modifier,
    accentColor: Color = DriveBarOutline,
    accentWidth: Dp = 3.dp,
) {
    Row(
        modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
            .background(DriveBarBrush, BottomArcShape)
            .arcOutline(accentColor, accentWidth, ::bottomArcOutlinePath)
    ) {}
}
