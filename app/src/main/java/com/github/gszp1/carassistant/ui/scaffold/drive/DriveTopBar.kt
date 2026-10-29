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
import com.github.gszp1.carassistant.ui.components.Clock
import com.github.gszp1.carassistant.ui.theme.DriveAccent
import com.github.gszp1.carassistant.ui.theme.DriveTopBarSurface

@Composable
fun DriveTopBar(
    modifier: Modifier = Modifier,
    accentColor: Color = DriveAccent,
    accentWidth: Dp = 5.dp,
) {
    Row(
        modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
            .background(DriveTopBarSurface, TopArcShape)
            .arcOutline(accentColor, accentWidth, ::topArcOutlinePath)
    ) {
        Clock()
    }
}
