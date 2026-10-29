package com.github.gszp1.carassistant.ui.scaffold.drive

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp

internal val TopArcShape = GenericShape { size, _ ->
    val edge = size.height * 0.6f
    moveTo(0f, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width, edge)
    quadraticTo(size.width / 2, size.height * 1.4f, 0f, edge)
    close()
}

internal val BottomArcShape = GenericShape { size, _ ->
    val edge = size.height * 0.4f
    moveTo(0f, size.height)
    lineTo(size.width, size.height)
    lineTo(size.width, edge)
    quadraticTo(size.width / 2, size.height * -0.4f, 0f, edge)
    close()
}

internal fun topArcOutlinePath(size: Size) = Path().apply {
    val edge = size.height * 0.6f
    moveTo(size.width, edge)
    quadraticTo(size.width / 2, size.height * 1.4f, 0f, edge)
}

internal fun bottomArcOutlinePath(size: Size) = Path().apply {
    val edge = size.height * 0.4f
    moveTo(size.width, edge)
    quadraticTo(size.width / 2, size.height * -0.4f, 0f, edge)
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
