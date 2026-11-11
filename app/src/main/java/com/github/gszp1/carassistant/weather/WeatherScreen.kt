package com.github.gszp1.carassistant.weather

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.gszp1.carassistant.ui.theme.CarAssistantTheme
import com.github.gszp1.carassistant.ui.theme.DrivePalette
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette
import com.github.gszp1.carassistant.ui.theme.Styles

@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    palette: DrivePalette = LocalDrivePalette.current,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = palette.contentCenter,
        contentColor = palette.barLabel,
    ) {}
}

@Preview(name = "weather-screen", widthDp = 960, heightDp = 540)
@Composable
private fun WeatherScreenPreview() {
    CarAssistantTheme(style = Styles.AQUAMARINE) {
        WeatherScreen()
    }
}
