package com.github.gszp1.carassistant.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

/** Ułamek wysokości belki, na którym czerń krawędzi przechodzi w ciemny odcień akcentu. */
private const val BarMidStop = 0.7f

enum class Styles {
    AQUAMARINE,
    AMBER_SHIFT,
    EMERALD_CORE,
    RUBY_DYNAMIC;

    val palette: DrivePalette
        get() = when (this) {
            AQUAMARINE -> AquamarinePalette
            AMBER_SHIFT -> AmberShiftPalette
            EMERALD_CORE -> EmeraldCorePalette
            RUBY_DYNAMIC -> RubyDynamicPalette
        }

    fun next(): Styles = entries[(ordinal + 1) % entries.size]
}

@Immutable
data class DrivePalette(
    val style: Styles,
    val barEdge: Color,
    val barMid: Color,
    val barCenter: Color,
    val barOutline: Color,
    val barLabel: Color,
    val iconAccent: Color,
    val gridLine: Color,
    val contentCenter: Color,
    val contentMid: Color,
    val contentEdge: Color,
) {
    /**
     * Kolor wypełnienia belki dla ułamka wysokości liczonego od prostej krawędzi ekranu (0f)
     * do krzywej belki (1f).
     */
    fun barColorAt(fraction: Float): Color =
        if (fraction <= BarMidStop) {
            lerp(barEdge, barMid, fraction / BarMidStop)
        } else {
            lerp(barMid, barCenter, (fraction - BarMidStop) / (1f - BarMidStop))
        }

    val contentBackdropBrush: Brush = Brush.verticalGradient(
        0.0f to contentEdge,
        0.13f to contentMid,
        0.92f to contentMid,
        1.0f to contentEdge,
    )
}

private fun drivePalette(
    style: Styles,
    barMid: Color,
    barCenter: Color,
    barOutline: Color,
    barLabel: Color,
    gridLine: Color,
) = DrivePalette(
    style = style,
    barEdge = DriveBarEdge,
    barMid = barMid,
    barCenter = barCenter,
    barOutline = barOutline,
    barLabel = barLabel,
    iconAccent = barCenter,
    gridLine = gridLine,
    contentCenter = DriveContentCenter,
    contentMid = DriveContentMid,
    contentEdge = DriveContentEdge,
)

private val AquamarinePalette = drivePalette(
    style = Styles.AQUAMARINE,
    barMid = Color(0xFF063A45),
    barCenter = Color(0xFF1BA9BF),
    barOutline = Color(0xFF0B6470),
    barLabel = Color(0xFF9FB4B8),
    gridLine = Color(0xFF0E3A42),
)

private val AmberShiftPalette = drivePalette(
    style = Styles.AMBER_SHIFT,
    barMid = Color(0xFF452806),
    barCenter = Color(0xFFF2A51C),
    barOutline = Color(0xFF7A5410),
    barLabel = Color(0xFFB8AC9F),
    gridLine = Color(0xFF3F2D0A),
)

private val EmeraldCorePalette = drivePalette(
    style = Styles.EMERALD_CORE,
    barMid = Color(0xFF064529),
    barCenter = Color(0xFF1BBF7E),
    barOutline = Color(0xFF0B7045),
    barLabel = Color(0xFF9FB8AA),
    gridLine = Color(0xFF0E4229),
)

private val RubyDynamicPalette = drivePalette(
    style = Styles.RUBY_DYNAMIC,
    barMid = Color(0xFF450612),
    barCenter = Color(0xFFBF1B3E),
    barOutline = Color(0xFF700B22),
    barLabel = Color(0xFFB89FA5),
    gridLine = Color(0xFF420E1C),
)

val LocalDrivePalette = staticCompositionLocalOf { Styles.AQUAMARINE.palette }