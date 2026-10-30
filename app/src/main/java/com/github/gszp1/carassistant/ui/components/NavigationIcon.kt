package com.github.gszp1.carassistant.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.gszp1.carassistant.ui.theme.DriveContentCenter
import com.github.gszp1.carassistant.ui.theme.DriveIconAccent

val NavigationArrow: ImageVector = ImageVector.Builder(
    name = "NavigationArrow",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
).apply {
    path(
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.6f,
        strokeLineCap = StrokeCap.Round,
        strokeLineJoin = StrokeJoin.Round,
    ) {
        moveTo(12f, 2.2f)
        arcToRelative(9.8f, 9.8f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, 19.6f)
        arcToRelative(9.8f, 9.8f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, -19.6f)
        close()
    }
    path(
        fill = SolidColor(Color.Black),
        strokeLineJoin = StrokeJoin.Round,
    ) {
        moveTo(12f, 5f)
        lineTo(17.6f, 18f)
        lineTo(12f, 15f)
        lineTo(6.4f, 18f)
        close()
    }
}.build()

@Composable
fun NavigationIcon(
    modifier: Modifier = Modifier,
    tint: Color = DriveIconAccent,
) {
    Icon(
        imageVector = NavigationArrow,
        contentDescription = "Nawigacja",
        modifier = modifier,
        tint = tint,
    )
}

@Preview(name = "Ikona nawigacji", showBackground = false)
@Composable
private fun NavigationIconPreview() {
    NavigationIcon(
        modifier = Modifier
            .background(DriveContentCenter)
            .padding(12.dp)
            .size(72.dp)
    )
}
