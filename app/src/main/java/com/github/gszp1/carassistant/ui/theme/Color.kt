package com.github.gszp1.carassistant.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val DriveBackground = Color(0xFFCBCBCB)

val DriveTopBarSurface = Color(0xFF6E6E6E)

val DriveBottomBarSurface = Color(0xFF737373)

val DriveAccent = Color(0xFF00BCD4)

val DriveBarOutline = Color(0xFF0B6470)

val DriveBarEdge = Color(0xFF000000)

val DriveBarMid = Color(0xFF063A45)

val DriveBarCenter = Color(0xFF1BA9BF)

val DriveBarBrush: Brush = Brush.horizontalGradient(
    0.0f to DriveBarEdge,
    0.28f to DriveBarMid,
    0.5f to DriveBarCenter,
    0.72f to DriveBarMid,
    1.0f to DriveBarEdge,
)

val DriveContentCenter = Color(0xFF1E1E1E)

val DriveContentMid = Color(0xFF2E2E2E)

val DriveContentEdge = Color(0xFF464646)

val DriveGridLine = Color(0xFF0E3A42)

val DriveIconAccent = DriveBarCenter

val DriveBarLabel = Color(0xFF9FB4B8)

val DriveContentBackdropBrush: Brush = Brush.verticalGradient(
    0.0f to DriveContentEdge,
    0.13f to DriveContentMid,
    0.92f to DriveContentMid,
    1.0f to DriveContentEdge,
)