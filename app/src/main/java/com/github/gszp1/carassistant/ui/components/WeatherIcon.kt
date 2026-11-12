package com.github.gszp1.carassistant.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.github.gszp1.carassistant.R
import com.github.gszp1.carassistant.ui.theme.CarAssistantTheme
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

enum class WeatherSymbol(
    @DrawableRes val drawable: Int,
    val description: String,
    @RawRes val animation: Int? = null,
) {
    CLEAR_DAY(R.drawable.wi_clear_day, "Clear", R.raw.wi_clear_day),
    CLEAR_NIGHT(R.drawable.wi_clear_night, "Clear", R.raw.wi_clear_night),
    PARTLY_CLOUDY_DAY(R.drawable.wi_partly_cloudy_day, "Partly cloudy", R.raw.wi_partly_cloudy_day),
    PARTLY_CLOUDY_NIGHT(R.drawable.wi_partly_cloudy_night, "Partly cloudy", R.raw.wi_partly_cloudy_night),
    OVERCAST(R.drawable.wi_overcast, "Overcast", R.raw.wi_overcast),
    OVERCAST_DAY(R.drawable.wi_overcast_day, "Overcast", R.raw.wi_overcast_day),
    OVERCAST_NIGHT(R.drawable.wi_overcast_night, "Overcast", R.raw.wi_overcast_night),
    CLOUDY(R.drawable.wi_cloudy, "Cloudy", R.raw.wi_cloudy),
    FOG(R.drawable.wi_fog, "Fog", R.raw.wi_fog),
    FOG_DAY(R.drawable.wi_fog_day, "Fog", R.raw.wi_fog_day),
    FOG_NIGHT(R.drawable.wi_fog_night, "Fog", R.raw.wi_fog_night),
    HAZE(R.drawable.wi_haze, "Haze", R.raw.wi_haze),
    MIST(R.drawable.wi_mist, "Mist", R.raw.wi_mist),
    DRIZZLE(R.drawable.wi_drizzle, "Drizzle", R.raw.wi_drizzle),
    RAIN(R.drawable.wi_rain, "Rain", R.raw.wi_rain),
    SLEET(R.drawable.wi_sleet, "Sleet", R.raw.wi_sleet),
    SNOW(R.drawable.wi_snow, "Snow", R.raw.wi_snow),
    HAIL(R.drawable.wi_hail, "Hail", R.raw.wi_hail),
    THUNDERSTORMS(R.drawable.wi_thunderstorms, "Thunderstorms", R.raw.wi_thunderstorms),
    THUNDERSTORMS_RAIN(R.drawable.wi_thunderstorms_rain, "Thunderstorms with rain", R.raw.wi_thunderstorms_rain),
    WIND(R.drawable.wi_wind, "Wind", R.raw.wi_wind),
    TORNADO(R.drawable.wi_tornado, "Tornado", R.raw.wi_tornado),
    NOT_AVAILABLE(R.drawable.wi_not_available, "No weather data", R.raw.wi_not_available),
    THERMOMETER(R.drawable.wi_thermometer, "Temperature"),
    BAROMETER(R.drawable.wi_barometer, "Pressure"),
    PRESSURE_HIGH(R.drawable.wi_pressure_high, "High pressure"),
    PRESSURE_LOW(R.drawable.wi_pressure_low, "Low pressure"),
    HUMIDITY(R.drawable.wi_humidity, "Humidity"),
    RAINDROPS(R.drawable.wi_raindrops, "Precipitation"),
    UMBRELLA(R.drawable.wi_umbrella, "Precipitation probability"),
    WINDSOCK(R.drawable.wi_windsock, "Wind speed"),
    COMPASS(R.drawable.wi_compass, "Wind direction"),
    HORIZON(R.drawable.wi_horizon, "Visibility"),
    SUNRISE(R.drawable.wi_sunrise, "Sunrise"),
    SUNSET(R.drawable.wi_sunset, "Sunset"),
    UV_INDEX(R.drawable.wi_uv_index, "UV index"),
}

@Composable
fun WeatherIcon(
    symbol: WeatherSymbol = WeatherSymbol.PARTLY_CLOUDY_DAY,
    modifier: Modifier = Modifier,
    tint: Color? = null,
    animated: Boolean = true,
) {
    val animation = symbol.animation
    val playable = animated && animation != null && tint == null && !LocalInspectionMode.current
    if (playable) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animation!!))
        LottieAnimation(
            composition = composition,
            modifier = modifier.semantics { contentDescription = symbol.description },
            iterations = LottieConstants.IterateForever,
        )
    } else {
        Image(
            painter = painterResource(symbol.drawable),
            contentDescription = symbol.description,
            modifier = modifier,
            colorFilter = tint?.let(ColorFilter::tint),
        )
    }
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
