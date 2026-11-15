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
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.gszp1.carassistant.ui.theme.CarAssistantTheme
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette
import com.github.gszp1.carassistant.ui.theme.Styles

private const val HandsetScale = 0.62f

val PhoneHandset: ImageVector = ImageVector.Builder(
    name = "PhoneHandset",
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
    group(
        scaleX = HandsetScale,
        scaleY = HandsetScale,
        pivotX = 12f,
        pivotY = 12f,
    ) {
        path(
            fill = SolidColor(Color.Black),
            strokeLineJoin = StrokeJoin.Round,
        ) {
            moveTo(6.62f, 10.79f)
            curveToRelative(1.44f, 2.83f, 3.76f, 5.14f, 6.59f, 6.59f)
            lineToRelative(2.2f, -2.2f)
            curveToRelative(0.27f, -0.27f, 0.67f, -0.36f, 1.02f, -0.24f)
            curveToRelative(1.12f, 0.37f, 2.33f, 0.57f, 3.57f, 0.57f)
            curveToRelative(0.55f, 0f, 1f, 0.45f, 1f, 1f)
            verticalLineTo(20f)
            curveToRelative(0f, 0.55f, -0.45f, 1f, -1f, 1f)
            curveToRelative(-9.39f, 0f, -17f, -7.61f, -17f, -17f)
            curveToRelative(0f, -0.55f, 0.45f, -1f, 1f, -1f)
            horizontalLineToRelative(3.5f)
            curveToRelative(0.55f, 0f, 1f, 0.45f, 1f, 1f)
            curveToRelative(0f, 1.25f, 0.2f, 2.45f, 0.57f, 3.57f)
            curveToRelative(0.11f, 0.35f, 0.03f, 0.74f, -0.25f, 1.02f)
            lineToRelative(-2.2f, 2.2f)
            close()
        }
    }
}.build()

@Composable
fun PhoneIcon(
    modifier: Modifier = Modifier,
    tint: Color = LocalDrivePalette.current.iconAccent,
) {
    Icon(
        imageVector = PhoneHandset,
        contentDescription = "Phone",
        modifier = modifier,
        tint = tint,
    )
}

@Preview(name = "phone-icon", showBackground = false)
@Composable
private fun PhoneIconPreview() {
    CarAssistantTheme(style = Styles.AQUAMARINE) {
        PhoneIcon(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp)
                .size(72.dp)
        )
    }
}
