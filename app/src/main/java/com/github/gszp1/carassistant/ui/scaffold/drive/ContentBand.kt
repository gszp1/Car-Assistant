package com.github.gszp1.carassistant.ui.scaffold.drive

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import com.github.gszp1.carassistant.ui.theme.DriveContentBackdropBrush
import com.github.gszp1.carassistant.ui.theme.DriveContentCenter
import com.github.gszp1.carassistant.ui.theme.DriveContentMid
import com.github.gszp1.carassistant.ui.theme.DriveGridLine

private const val BandCenterFraction = 0.525f
private const val BandHalfCenter = 0.30f
private const val BandHalfEnd = 0.375f
private const val BandCoreScale = 0.50f
private const val BandSteps = 96

private fun contentBandPath(size: Size, scale: Float): Path {
    val centerY = size.height * BandCenterFraction
    val halfCenter = size.height * BandHalfCenter * scale
    val halfEnd = size.height * BandHalfEnd * scale
    val top = centerY - halfEnd
    val bottom = centerY + halfEnd
    return Path().apply {
        moveTo(0f, top)
        quadraticTo(size.width / 2f, centerY - 2f * halfCenter + halfEnd, size.width, top)
        lineTo(size.width, bottom)
        quadraticTo(size.width / 2f, centerY + 2f * halfCenter - halfEnd, 0f, bottom)
        close()
    }
}

private fun smoothStep(fraction: Float) = fraction * fraction * (3f - 2f * fraction)

private fun contentBandLayers(size: Size): List<Pair<Path, Color>> =
    List(BandSteps) { step ->
        val fraction = step / (BandSteps - 1f)
        val scale = 1f - (1f - BandCoreScale) * fraction
        contentBandPath(size, scale) to
            lerp(DriveContentMid, DriveContentCenter, smoothStep(fraction))
    }

internal fun Modifier.driveContentBackground() = drawWithCache {
    val layers = contentBandLayers(size)
    val grid = hexGridPath(size, HexRadius.toPx())
    val gridStroke = Stroke(width = HexStrokeWidth.toPx())
    onDrawBehind {
        drawRect(DriveContentBackdropBrush)
        layers.forEach { (path, color) -> drawPath(path, color) }
        drawPath(grid, DriveGridLine, style = gridStroke)
    }
}
