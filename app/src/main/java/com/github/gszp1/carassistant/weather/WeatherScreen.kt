package com.github.gszp1.carassistant.weather

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.gszp1.carassistant.ui.components.WeatherIcon
import com.github.gszp1.carassistant.ui.scaffold.drive.driveContentBackground
import com.github.gszp1.carassistant.ui.theme.CarAssistantTheme
import com.github.gszp1.carassistant.ui.theme.DrivePalette
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette
import com.github.gszp1.carassistant.ui.theme.Styles

private val BackButtonPadding = 16.dp
private val BackButtonSize = 52.dp
private val BackButtonBorderWidth = 1.5.dp
private val BackIconSize = 32.dp

private val ContentShiftX = 48.dp
private val ContentShiftY = 40.dp
private val WeatherCircleSize = 220.dp
private val WeatherCircleBorderWidth = 1.5.dp
private val WeatherIconSize = 128.dp
private val LabelIndent = 32.dp
private val LabelSpacing = 20.dp
private val LabelToValueGap = 2.dp

private data class WeatherMetric(val label: String, val value: String)

private val WeatherMetrics = listOf(
    WeatherMetric("Temperature", "21°C"),
    WeatherMetric("Pressure", "1013 hPa"),
    WeatherMetric("Humidity", "58%"),
    WeatherMetric("Precipitation probability", "20%"),
)

@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    palette: DrivePalette = LocalDrivePalette.current,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.Transparent,
        contentColor = palette.barLabel,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .driveContentBackground(palette),
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(BackButtonPadding)
                    .size(BackButtonSize)
                    .border(BackButtonBorderWidth, palette.barOutline, CircleShape),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(BackIconSize),
                    tint = palette.iconAccent,
                )
            }
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = -ContentShiftX, y = -ContentShiftY),
                verticalAlignment = Alignment.Top,
            ) {
                Box(
                    modifier = Modifier
                        .size(WeatherCircleSize)
                        .border(WeatherCircleBorderWidth, palette.barOutline, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    WeatherIcon(modifier = Modifier.size(WeatherIconSize))
                }
                Spacer(Modifier.width(LabelIndent))
                Column(
                    verticalArrangement = Arrangement.spacedBy(LabelSpacing),
                ) {
                    WeatherMetrics.forEach { metric ->
                        Column(verticalArrangement = Arrangement.spacedBy(LabelToValueGap)) {
                            Text(text = metric.label, style = MaterialTheme.typography.labelMedium)
                            Text(text = metric.value, style = MaterialTheme.typography.titleLarge)
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "weather-screen", widthDp = 960, heightDp = 540)
@Composable
private fun WeatherScreenPreview() {
    CarAssistantTheme(style = Styles.AQUAMARINE) {
        WeatherScreen()
    }
}
