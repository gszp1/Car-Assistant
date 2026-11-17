package com.github.gszp1.carassistant.ui.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

class RoundedTrapezoidShape(
    private val slant: Dp,
    private val cornerRadius: Dp,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val slantPx = with(density) { slant.toPx() }
        val radiusPx = with(density) { cornerRadius.toPx() }
        val corners = listOf(
            Offset(0f, 0f),
            Offset(size.width, 0f),
            Offset(size.width - slantPx, size.height),
            Offset(slantPx, size.height),
        )
        return Outline.Generic(Path().apply { roundedPolygon(corners, radiusPx) })
    }
}

private fun Path.roundedPolygon(corners: List<Offset>, radius: Float) {
    corners.forEachIndexed { index, corner ->
        val previous = corners[(index + corners.size - 1) % corners.size]
        val next = corners[(index + 1) % corners.size]
        val limit = minOf(
            radius,
            (previous - corner).getDistance() / 2f,
            (next - corner).getDistance() / 2f,
        )
        val from = corner.towards(previous, limit)
        val to = corner.towards(next, limit)
        if (index == 0) moveTo(from.x, from.y) else lineTo(from.x, from.y)
        quadraticTo(corner.x, corner.y, to.x, to.y)
    }
    close()
}

private fun Offset.towards(target: Offset, distance: Float): Offset {
    val delta = target - this
    val length = delta.getDistance()
    return if (length == 0f) this else this + delta / length * distance
}
