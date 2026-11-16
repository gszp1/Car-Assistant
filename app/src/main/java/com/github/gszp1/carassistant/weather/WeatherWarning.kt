package com.github.gszp1.carassistant.weather

private const val ViolentGustsKmh = 90.0
private const val StrongGustsKmh = 60.0
private const val LowVisibilityMeters = 1_000.0
private const val FreezingCelsius = 1.0

private val FreezingConditions = setOf(
    WeatherCondition.FREEZING_DRIZZLE,
    WeatherCondition.FREEZING_RAIN,
    WeatherCondition.RIME_FOG,
)

private val HeavyRainConditions = setOf(
    WeatherCondition.HEAVY_RAIN,
    WeatherCondition.HEAVY_RAIN_SHOWERS,
)

private val HeavySnowConditions = setOf(
    WeatherCondition.HEAVY_SNOW,
    WeatherCondition.SNOW_SHOWERS,
)

private val ThunderstormConditions = setOf(
    WeatherCondition.THUNDERSTORMS,
    WeatherCondition.THUNDERSTORMS_HAIL,
)

private val Warnings: List<Pair<String, (CurrentWeather) -> Boolean>> = listOf(
    "Thunderstorms" to { it.condition in ThunderstormConditions },
    "Freezing precipitation" to { it.condition in FreezingConditions },
    "Violent wind gusts" to { it.windGustsKmh >= ViolentGustsKmh },
    "Heavy snow" to { it.condition in HeavySnowConditions },
    "Heavy rain" to { it.condition in HeavyRainConditions },
    "Icy roads" to { it.temperatureCelsius <= FreezingCelsius && it.precipitationMillimeters > 0.0 },
    "Strong wind gusts" to { it.windGustsKmh >= StrongGustsKmh },
    "Low visibility" to { (it.visibilityMeters ?: Double.MAX_VALUE) <= LowVisibilityMeters },
)

internal fun warningFor(weather: CurrentWeather): String? =
    Warnings.firstOrNull { (_, matches) -> matches(weather) }?.first
