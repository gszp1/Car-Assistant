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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.gszp1.carassistant.ui.theme.CarAssistantTheme
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette
import com.github.gszp1.carassistant.ui.theme.Styles

val PartlyCloudyDay: ImageVector = ImageVector.Builder(
    name = "PartlyCloudyDay",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
).apply {
    path(
        stroke = SolidColor(Color.Black),
        strokeLineWidth = 1.3f,
        strokeLineCap = StrokeCap.Round,
    ) {
        moveTo(14.5f, 3.4f)
        lineTo(14.5f, 2f)
        moveTo(17.05f, 4.45f)
        lineTo(18.04f, 3.46f)
        moveTo(18.1f, 7f)
        lineTo(19.5f, 7f)
        moveTo(17.05f, 9.55f)
        lineTo(18.04f, 10.54f)
        moveTo(14.5f, 10.6f)
        lineTo(14.5f, 12f)
        moveTo(11.95f, 9.55f)
        lineTo(10.96f, 10.54f)
        moveTo(10.9f, 7f)
        lineTo(9.5f, 7f)
        moveTo(11.95f, 4.45f)
        lineTo(10.96f, 3.46f)
    }
    path(fill = SolidColor(Color.Black)) {
        moveTo(11.5f, 7f)
        arcToRelative(3f, 3f, 0f, isMoreThanHalf = true, isPositiveArc = true, 6f, 0f)
        arcToRelative(3f, 3f, 0f, isMoreThanHalf = true, isPositiveArc = true, -6f, 0f)
        close()
    }
    path(fill = SolidColor(Color.Black)) {
        moveTo(17.15f, 16.23f)
        curveTo(16.67f, 13.81f, 14.55f, 12f, 12f, 12f)
        curveTo(9.98f, 12f, 8.22f, 13.15f, 7.35f, 14.83f)
        curveTo(5.24f, 15.05f, 3.6f, 16.84f, 3.6f, 19f)
        curveToRelative(0f, 2.317f, 1.883f, 4.2f, 4.2f, 4.2f)
        horizontalLineToRelative(9.1f)
        curveToRelative(1.932f, 0f, 3.5f, -1.568f, 3.5f, -3.5f)
        curveToRelative(0f, -1.848f, -1.435f, -3.346f, -3.248f, -3.472f)
        close()
    }
}.build()

@Composable
fun WeatherIcon(
    modifier: Modifier = Modifier,
    tint: Color = LocalDrivePalette.current.iconAccent,
) {
    Icon(
        imageVector = PartlyCloudyDay,
        contentDescription = "Weather",
        modifier = modifier,
        tint = tint,
    )
}

@Preview(name = "weather-icon", showBackground = false)
@Composable
private fun WeatherIconPreview() {
    CarAssistantTheme(style = Styles.AQUAMARINE) {
        WeatherIcon(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp)
                .size(72.dp)
        )
    }
}
