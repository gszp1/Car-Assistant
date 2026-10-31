package com.github.gszp1.carassistant.ui.scaffold.drive

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import com.github.gszp1.carassistant.ui.theme.DrivePalette

private const val TopArcEdgeFraction = 0.6f
private const val TopArcControlFraction = 1.4f
private const val BottomArcEdgeFraction = 0.4f
private const val BottomArcControlFraction = -0.4f
private const val ArcGradientSteps = 96

internal fun topArcPath(size: Size, scale: Float = 1f) = Path().apply {
    val edge = size.height * TopArcEdgeFraction * scale
    moveTo(0f, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width, edge)
    quadraticTo(size.width / 2f, size.height * TopArcControlFraction * scale, 0f, edge)
    close()
}

internal fun bottomArcPath(size: Size, scale: Float = 1f) = Path().apply {
    val edge = size.height * (1f - (1f - BottomArcEdgeFraction) * scale)
    val control = size.height * (1f - (1f - BottomArcControlFraction) * scale)
    moveTo(0f, size.height)
    lineTo(size.width, size.height)
    lineTo(size.width, edge)
    quadraticTo(size.width / 2f, control, 0f, edge)
    close()
}

internal fun topArcOutlinePath(size: Size) = Path().apply {
    val edge = size.height * TopArcEdgeFraction
    moveTo(size.width, edge)
    quadraticTo(size.width / 2f, size.height * TopArcControlFraction, 0f, edge)
}

internal fun bottomArcOutlinePath(size: Size) = Path().apply {
    val edge = size.height * BottomArcEdgeFraction
    moveTo(size.width, edge)
    quadraticTo(size.width / 2f, size.height * BottomArcControlFraction, 0f, edge)
}

internal fun Modifier.arcBarBackground(
    palette: DrivePalette,
    arcPath: (Size, Float) -> Path,
) = drawWithCache {
    val layers = List(ArcGradientSteps) { step ->
        val fraction = 1f - step / (ArcGradientSteps - 1f)
        arcPath(size, fraction) to palette.barColorAt(fraction)
    }
    onDrawBehind {
        layers.forEach { (path, color) -> drawPath(path, color) }
    }
}

internal fun Modifier.arcOutline(
    color: Color,
    width: Dp,
    outline: (Size) -> Path,
) = drawWithContent {
    drawContent()
    drawPath(
        path = outline(size),
        color = color,
        style = Stroke(width = width.toPx(), cap = StrokeCap.Round)
    )
}
