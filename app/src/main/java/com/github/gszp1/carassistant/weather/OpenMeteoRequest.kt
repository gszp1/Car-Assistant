package com.github.gszp1.carassistant.weather

import java.util.Locale

internal object OpenMeteoRequest {

    private const val ForecastEndpoint = "https://api.open-meteo.com/v1/forecast"

    private const val CoordinateFormat = "%.4f"

    private val CurrentVariables = listOf(
        "temperature_2m",
        "apparent_temperature",
        "relative_humidity_2m",
        "is_day",
        "weather_code",
        "precipitation",
        "pressure_msl",
        "wind_speed_10m",
        "wind_direction_10m",
        "wind_gusts_10m",
    )

    private val HourlyVariables = listOf(
        "visibility",
        "precipitation_probability",
    )

    fun forecastUrl(point: GeoPoint): String {
        val parameters = linkedMapOf(
            "latitude" to point.latitude.asCoordinate(),
            "longitude" to point.longitude.asCoordinate(),
            "current" to CurrentVariables.joinToString(","),
            "hourly" to HourlyVariables.joinToString(","),
            "forecast_days" to "1",
            "timezone" to "auto",
            "timeformat" to "unixtime",
            "temperature_unit" to "celsius",
            "wind_speed_unit" to "kmh",
            "precipitation_unit" to "mm",
        )
        return parameters.entries.joinToString(
            separator = "&",
            prefix = "$ForecastEndpoint?",
        ) { (name, value) -> "$name=$value" }
    }

    private fun Double.asCoordinate(): String = String.format(Locale.ROOT, CoordinateFormat, this)
}
