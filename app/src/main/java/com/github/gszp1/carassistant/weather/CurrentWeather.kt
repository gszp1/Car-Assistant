package com.github.gszp1.carassistant.weather

import androidx.compose.runtime.Immutable
import com.github.gszp1.carassistant.ui.components.WeatherSymbol
import java.time.Instant
import kotlin.math.roundToInt

data class GeoPoint(
    val latitude: Double,
    val longitude: Double,
)

enum class WindDirection(val label: String) {
    N("N"), NNE("NNE"), NE("NE"), ENE("ENE"),
    E("E"), ESE("ESE"), SE("SE"), SSE("SSE"),
    S("S"), SSW("SSW"), SW("SW"), WSW("WSW"),
    W("W"), WNW("WNW"), NW("NW"), NNW("NNW"),
    ;

    companion object {
        private const val SectorDegrees = 360.0 / 16

        fun fromDegrees(degrees: Double): WindDirection {
            val sector = (degrees.mod(360.0) / SectorDegrees).roundToInt()
            return entries[sector.mod(entries.size)]
        }
    }
}

@Immutable
data class CurrentWeather(
    val observedAt: Instant,
    val isDay: Boolean,
    val condition: WeatherCondition,
    val temperatureCelsius: Double,
    val apparentTemperatureCelsius: Double,
    val relativeHumidityPercent: Int,
    val pressureHectopascals: Double,
    val windSpeedKmh: Double,
    val windGustsKmh: Double,
    val windBearingDegrees: Int,
    val precipitationMillimeters: Double,
    val visibilityMeters: Double?,
    val precipitationProbabilityPercent: Int?,
) {
    val symbol: WeatherSymbol get() = condition.symbol(isDay)

    val windDirection: WindDirection get() = WindDirection.fromDegrees(windBearingDegrees.toDouble())
}
