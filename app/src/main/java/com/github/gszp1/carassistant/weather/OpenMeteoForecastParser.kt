package com.github.gszp1.carassistant.weather

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.time.Instant

internal fun parseCurrentWeather(payload: String): CurrentWeather {
    val root = try {
        JSONObject(payload)
    } catch (e: JSONException) {
        throw OpenMeteoException("Open-Meteo returned a malformed response", e)
    }
    if (root.optBoolean("error")) {
        throw OpenMeteoException(root.optString("reason").ifEmpty { "Open-Meteo rejected the request" })
    }

    val current = root.optJSONObject("current")
        ?: throw OpenMeteoException("Open-Meteo response carries no \"current\" block")
    val observedAt = Instant.ofEpochSecond(current.requireNumber("time").toLong())

    val hourly = root.optJSONObject("hourly")
    val hourIndex = hourly?.let { hourIndexAt(it, observedAt) }

    return CurrentWeather(
        observedAt = observedAt,
        isDay = current.requireNumber("is_day").toInt() != 0,
        condition = WeatherCondition.fromWmoCode(current.requireNumber("weather_code").toInt()),
        temperatureCelsius = current.requireNumber("temperature_2m").toDouble(),
        apparentTemperatureCelsius = current.requireNumber("apparent_temperature").toDouble(),
        relativeHumidityPercent = current.requireNumber("relative_humidity_2m").toInt(),
        pressureHectopascals = current.requireNumber("pressure_msl").toDouble(),
        windSpeedKmh = current.requireNumber("wind_speed_10m").toDouble(),
        windGustsKmh = current.requireNumber("wind_gusts_10m").toDouble(),
        windBearingDegrees = current.requireNumber("wind_direction_10m").toInt(),
        precipitationMillimeters = current.requireNumber("precipitation").toDouble(),
        visibilityMeters = hourly.hourlyNumber("visibility", hourIndex)?.toDouble(),
        precipitationProbabilityPercent = hourly.hourlyNumber("precipitation_probability", hourIndex)?.toInt(),
    )
}

private fun hourIndexAt(hourly: JSONObject, observedAt: Instant): Int? {
    val times: JSONArray = hourly.optJSONArray("time") ?: return null
    if (times.length() == 0) return null

    var index = 0
    for (candidate in 0 until times.length()) {
        if (times.optLong(candidate) > observedAt.epochSecond) break
        index = candidate
    }
    return index
}

private fun JSONObject.requireNumber(name: String): Number =
    opt(name) as? Number
        ?: throw OpenMeteoException("Open-Meteo response is missing \"$name\"")

private fun JSONObject?.hourlyNumber(name: String, index: Int?): Number? {
    if (this == null || index == null) return null
    val values = optJSONArray(name) ?: return null
    if (index >= values.length() || values.isNull(index)) return null
    return values.opt(index) as? Number
}
