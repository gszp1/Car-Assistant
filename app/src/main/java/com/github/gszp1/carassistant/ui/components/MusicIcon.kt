package com.github.gszp1.carassistant.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.github.gszp1.carassistant.ui.theme.CarAssistantTheme
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette
import com.github.gszp1.carassistant.ui.theme.Styles

val MusicNote: ImageVector = ImageVector.Builder(
    name = "MusicNote",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
).apply {
    // Outer ring, matching NavigationArrow.
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
    // Beamed eighth notes: stems and beam.
    path(
        fill = SolidColor(Color.Black),
        strokeLineJoin = StrokeJoin.Round,
    ) {
        moveTo(9.4f, 15.6f)
        lineTo(9.4f, 7.8f)
        lineTo(16.6f, 6.2f)
        lineTo(16.6f, 14f)
        lineTo(15.4f, 14f)
        lineTo(15.4f, 8.9f)
        lineTo(10.6f, 9.95f)
        lineTo(10.6f, 15.6f)
        close()
    }
    // Note heads.
    path(fill = SolidColor(Color.Black)) {
        moveTo(8f, 14.2f)
        arcToRelative(1.9f, 1.6f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, 3.2f)
        arcToRelative(1.9f, 1.6f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, -3.2f)
        close()
        moveTo(14.6f, 12.6f)
        arcToRelative(1.9f, 1.6f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, 3.2f)
        arcToRelative(1.9f, 1.6f, 0f, isMoreThanHalf = true, isPositiveArc = true, 0f, -3.2f)
        close()
    }
}.build()

@Composable
fun MusicIcon(
    modifier: Modifier = Modifier,
    tint: Color = LocalDrivePalette.current.iconAccent,
) {
    Icon(
        imageVector = MusicNote,
        contentDescription = "Music",
        modifier = modifier,
        tint = tint,
    )
}

@Preview(name = "music-icon", showBackground = false)
@Composable
private fun MusicIconPreview() {
    CarAssistantTheme(style = Styles.AQUAMARINE) {
        MusicIcon(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp)
                .size(72.dp)
        )
    }
}
