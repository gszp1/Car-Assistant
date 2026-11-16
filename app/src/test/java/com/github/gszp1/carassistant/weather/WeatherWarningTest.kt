package com.github.gszp1.carassistant.weather

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.Instant

class WeatherWarningTest {

    @Test
    fun `stays silent on calm weather`() {
        assertNull(warningFor(CalmWeather))
    }

    @Test
    fun `warns about gusts once they get strong`() {
        assertNull(warningFor(CalmWeather.copy(windGustsKmh = 59.0)))
        assertEquals("Strong wind gusts", warningFor(CalmWeather.copy(windGustsKmh = 60.0)))
        assertEquals("Violent wind gusts", warningFor(CalmWeather.copy(windGustsKmh = 92.0)))
    }

    @Test
    fun `warns about hazardous conditions`() {
        assertEquals(
            "Thunderstorms",
            warningFor(CalmWeather.copy(condition = WeatherCondition.THUNDERSTORMS_HAIL)),
        )
        assertEquals(
            "Freezing precipitation",
            warningFor(CalmWeather.copy(condition = WeatherCondition.FREEZING_RAIN)),
        )
        assertEquals(
            "Heavy rain",
            warningFor(CalmWeather.copy(condition = WeatherCondition.HEAVY_RAIN_SHOWERS)),
        )
        assertEquals(
            "Icy roads",
            warningFor(CalmWeather.copy(temperatureCelsius = 0.5, precipitationMillimeters = 1.2)),
        )
        assertEquals("Low visibility", warningFor(CalmWeather.copy(visibilityMeters = 600.0)))
    }

    @Test
    fun `reports the most severe warning first`() {
        val stormyAndGusty = CalmWeather.copy(
            condition = WeatherCondition.THUNDERSTORMS,
            windGustsKmh = 95.0,
            visibilityMeters = 400.0,
        )

        assertEquals("Thunderstorms", warningFor(stormyAndGusty))
    }

    @Test
    fun `treats missing visibility as unremarkable`() {
        assertNull(warningFor(CalmWeather.copy(visibilityMeters = null)))
    }

    private companion object {
        val CalmWeather = CurrentWeather(
            observedAt = Instant.ofEpochSecond(1_763_294_400),
            isDay = true,
            condition = WeatherCondition.PARTLY_CLOUDY,
            temperatureCelsius = 21.3,
            apparentTemperatureCelsius = 20.1,
            relativeHumidityPercent = 58,
            pressureHectopascals = 1013.4,
            windSpeedKmh = 18.2,
            windGustsKmh = 32.0,
            windBearingDegrees = 310,
            precipitationMillimeters = 0.0,
            visibilityMeters = 12_000.0,
            precipitationProbabilityPercent = 20,
        )
    }
}
