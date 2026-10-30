package com.github.gszp1.carassistant.ui.scaffold.drive

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

internal val HexRadius = 28.dp
internal val HexStrokeWidth = 1.dp

private fun Path.addHexagon(centerX: Float, centerY: Float, radius: Float) {
    for (vertex in 0..5) {
        val angle = (PI / 3.0 * vertex - PI / 2.0).toFloat()
        val x = centerX + radius * cos(angle)
        val y = centerY + radius * sin(angle)
        if (vertex == 0) moveTo(x, y) else lineTo(x, y)
    }
    close()
}

internal fun hexGridPath(size: Size, radius: Float): Path {
    val stepX = sqrt(3f) * radius
    val stepY = 1.5f * radius
    val rows = ceil(size.height / stepY).toInt()
    val columns = ceil(size.width / stepX).toInt()
    return Path().apply {
        for (row in -1..rows) {
            val offsetX = if (row % 2 == 0) 0f else stepX / 2f
            for (column in -1..columns) {
                addHexagon(column * stepX + offsetX, row * stepY, radius)
            }
        }
    }
}
