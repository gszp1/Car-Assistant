package com.github.gszp1.carassistant.weather

import com.github.gszp1.carassistant.ui.components.WeatherSymbol

enum class WeatherCondition(
    val description: String,
    private val daySymbol: WeatherSymbol,
    private val nightSymbol: WeatherSymbol = daySymbol,
) {
    CLEAR("Clear", WeatherSymbol.CLEAR_DAY, WeatherSymbol.CLEAR_NIGHT),
    MAINLY_CLEAR("Mainly clear", WeatherSymbol.CLEAR_DAY, WeatherSymbol.CLEAR_NIGHT),
    PARTLY_CLOUDY("Partly cloudy", WeatherSymbol.PARTLY_CLOUDY_DAY, WeatherSymbol.PARTLY_CLOUDY_NIGHT),
    OVERCAST("Overcast", WeatherSymbol.OVERCAST_DAY, WeatherSymbol.OVERCAST_NIGHT),
    FOG("Fog", WeatherSymbol.FOG_DAY, WeatherSymbol.FOG_NIGHT),
    RIME_FOG("Freezing fog", WeatherSymbol.FOG_DAY, WeatherSymbol.FOG_NIGHT),
    DRIZZLE("Drizzle", WeatherSymbol.DRIZZLE),
    FREEZING_DRIZZLE("Freezing drizzle", WeatherSymbol.SLEET),
    LIGHT_RAIN("Light rain", WeatherSymbol.RAIN),
    RAIN("Rain", WeatherSymbol.RAIN),
    HEAVY_RAIN("Heavy rain", WeatherSymbol.RAIN),
    FREEZING_RAIN("Freezing rain", WeatherSymbol.SLEET),
    LIGHT_SNOW("Light snow", WeatherSymbol.SNOW),
    SNOW("Snow", WeatherSymbol.SNOW),
    HEAVY_SNOW("Heavy snow", WeatherSymbol.SNOW),
    SNOW_GRAINS("Snow grains", WeatherSymbol.SNOW),
    RAIN_SHOWERS("Rain showers", WeatherSymbol.RAIN),
    HEAVY_RAIN_SHOWERS("Heavy rain showers", WeatherSymbol.RAIN),
    SNOW_SHOWERS("Snow showers", WeatherSymbol.SNOW),
    THUNDERSTORMS("Thunderstorms", WeatherSymbol.THUNDERSTORMS_RAIN),
    THUNDERSTORMS_HAIL("Thunderstorms with hail", WeatherSymbol.HAIL),
    UNKNOWN("No weather data", WeatherSymbol.NOT_AVAILABLE),
    ;

    fun symbol(isDay: Boolean): WeatherSymbol = if (isDay) daySymbol else nightSymbol

    companion object {
        fun fromWmoCode(code: Int): WeatherCondition = when (code) {
            0 -> CLEAR
            1 -> MAINLY_CLEAR
            2 -> PARTLY_CLOUDY
            3 -> OVERCAST
            45 -> FOG
            48 -> RIME_FOG
            51, 53, 55 -> DRIZZLE
            56, 57 -> FREEZING_DRIZZLE
            61 -> LIGHT_RAIN
            63 -> RAIN
            65 -> HEAVY_RAIN
            66, 67 -> FREEZING_RAIN
            71 -> LIGHT_SNOW
            73 -> SNOW
            75 -> HEAVY_SNOW
            77 -> SNOW_GRAINS
            80, 81 -> RAIN_SHOWERS
            82 -> HEAVY_RAIN_SHOWERS
            85, 86 -> SNOW_SHOWERS
            95 -> THUNDERSTORMS
            96, 99 -> THUNDERSTORMS_HAIL
            else -> UNKNOWN
        }
    }
}
