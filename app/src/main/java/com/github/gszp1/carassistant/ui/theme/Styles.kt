package com.github.gszp1.carassistant.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

private const val BarMidStop = 0.7f

enum class DriveMode(val label: String) {
    DAY("Day"),
    NIGHT("Night"),
}

enum class Styles(val label: String) {
    AQUAMARINE("Aquamarine"),
    AMBER_SHIFT("Amber shift"),
    EMERALD_CORE("Emerald core"),
    RUBY_DYNAMIC("Ruby dynamic");

    fun palette(mode: DriveMode): DrivePalette = when (this) {
        AQUAMARINE -> AquamarinePalettes
        AMBER_SHIFT -> AmberShiftPalettes
        EMERALD_CORE -> EmeraldCorePalettes
        RUBY_DYNAMIC -> RubyDynamicPalettes
    }[mode]
}

@Immutable
data class DrivePalette(
    val style: Styles,
    val mode: DriveMode,
    val barEdge: Color,
    val barMid: Color,
    val barCenter: Color,
    val barOutline: Color,
    val barLabel: Color,
    val iconAccent: Color,
    val onAccent: Color,
    val gridLine: Color,
    val contentCenter: Color,
    val contentMid: Color,
    val contentEdge: Color,
) {
    fun barColorAt(fraction: Float): Color =
        if (fraction <= BarMidStop) {
            lerp(barEdge, barMid, fraction / BarMidStop)
        } else {
            lerp(barMid, barCenter, (fraction - BarMidStop) / (1f - BarMidStop))
        }

    val swatchBrush: Brush = Brush.linearGradient(
        0.0f to barCenter,
        0.5f to barOutline,
        1.0f to barMid,
    )

    val contentBackdropBrush: Brush = Brush.verticalGradient(
        0.0f to contentEdge,
        0.13f to contentMid,
        0.92f to contentMid,
        1.0f to contentEdge,
    )
}

private class DriveAccents(
    val mid: Color,
    val center: Color,
    val outline: Color,
    val label: Color,
    val grid: Color,
)

private class StylePalettes(val day: DrivePalette, val night: DrivePalette) {
    operator fun get(mode: DriveMode): DrivePalette =
        if (mode == DriveMode.NIGHT) night else day
}

private fun stylePalettes(
    style: Styles,
    night: DriveAccents,
    day: DriveAccents,
) = StylePalettes(
    day = DrivePalette(
        style = style,
        mode = DriveMode.DAY,
        barEdge = DriveDayBarEdge,
        barMid = day.mid,
        barCenter = day.center,
        barOutline = day.outline,
        barLabel = day.label,
        iconAccent = day.center,
        onAccent = DriveDayOnAccent,
        gridLine = day.grid,
        contentCenter = DriveDayContentCenter,
        contentMid = DriveDayContentMid,
        contentEdge = DriveDayContentEdge,
    ),
    night = DrivePalette(
        style = style,
        mode = DriveMode.NIGHT,
        barEdge = DriveNightBarEdge,
        barMid = night.mid,
        barCenter = night.center,
        barOutline = night.outline,
        barLabel = night.label,
        iconAccent = night.center,
        onAccent = DriveNightOnAccent,
        gridLine = night.grid,
        contentCenter = DriveNightContentCenter,
        contentMid = DriveNightContentMid,
        contentEdge = DriveNightContentEdge,
    ),
)

private val AquamarinePalettes = stylePalettes(
    style = Styles.AQUAMARINE,
    night = DriveAccents(
        mid = Color(0xFF063A45),
        center = Color(0xFF1BA9BF),
        outline = Color(0xFF0B6470),
        label = Color(0xFF9FB4B8),
        grid = Color(0xFF0E3A42),
    ),
    day = DriveAccents(
        mid = Color(0xFF9DBFC5),
        center = Color(0xFF0B606F),
        outline = Color(0xFF337E8A),
        label = Color(0xFF2E4A50),
        grid = Color(0xFF96B0B5),
    ),
)

private val AmberShiftPalettes = stylePalettes(
    style = Styles.AMBER_SHIFT,
    night = DriveAccents(
        mid = Color(0xFF452806),
        center = Color(0xFFF2A51C),
        outline = Color(0xFF7A5410),
        label = Color(0xFFB8AC9F),
        grid = Color(0xFF3F2D0A),
    ),
    day = DriveAccents(
        mid = Color(0xFFD4C09C),
        center = Color(0xFF774E03),
        outline = Color(0xFF9B6F18),
        label = Color(0xFF50412C),
        grid = Color(0xFFC3B391),
    ),
)

private val EmeraldCorePalettes = stylePalettes(
    style = Styles.EMERALD_CORE,
    night = DriveAccents(
        mid = Color(0xFF064529),
        center = Color(0xFF1BBF7E),
        outline = Color(0xFF0B7045),
        label = Color(0xFF9FB8AA),
        grid = Color(0xFF0E4229),
    ),
    day = DriveAccents(
        mid = Color(0xFFA2C4B4),
        center = Color(0xFF096440),
        outline = Color(0xFF2B7E59),
        label = Color(0xFF2C4A3C),
        grid = Color(0xFF96B8A7),
    ),
)

private val RubyDynamicPalettes = stylePalettes(
    style = Styles.RUBY_DYNAMIC,
    night = DriveAccents(
        mid = Color(0xFF450612),
        center = Color(0xFFBF1B3E),
        outline = Color(0xFF700B22),
        label = Color(0xFFB89FA5),
        grid = Color(0xFF420E1C),
    ),
    day = DriveAccents(
        mid = Color(0xFFD2ACB5),
        center = Color(0xFF9E1236),
        outline = Color(0xFFA83D56),
        label = Color(0xFF523038),
        grid = Color(0xFFC29CA7),
    ),
)

val LocalDrivePalette = staticCompositionLocalOf {
    Styles.AQUAMARINE.palette(DriveMode.NIGHT)
}
