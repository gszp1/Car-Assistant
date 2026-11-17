package com.github.gszp1.carassistant.weather

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class OpenMeteoForecastParserTest {

    @Test
    fun `maps the current block onto the weather model`() {
        val weather = parseCurrentWeather(ForecastPayload)

        assertEquals(Instant.ofEpochSecond(1_763_294_400), weather.observedAt)
        assertTrue(weather.isDay)
        assertEquals(WeatherCondition.PARTLY_CLOUDY, weather.condition)
        assertEquals(21.3, weather.temperatureCelsius, Tolerance)
        assertEquals(20.1, weather.apparentTemperatureCelsius, Tolerance)
        assertEquals(58, weather.relativeHumidityPercent)
        assertEquals(1013.4, weather.pressureHectopascals, Tolerance)
        assertEquals(18.2, weather.windSpeedKmh, Tolerance)
        assertEquals(47.5, weather.windGustsKmh, Tolerance)
        assertEquals(310, weather.windBearingDegrees)
        assertEquals(WindDirection.NW, weather.windDirection)
        assertEquals(0.8, weather.precipitationMillimeters, Tolerance)
    }

    @Test
    fun `picks the hourly entry covering the observation`() {
        val weather = parseCurrentWeather(ForecastPayload)

        assertEquals(8000.0, weather.visibilityMeters!!, Tolerance)
        assertEquals(20, weather.precipitationProbabilityPercent)
    }

    @Test
    fun `falls back to the first hour when the series starts in the future`() {
        val weather = parseCurrentWeather(ForecastPayload.replace("1763290800,1763294400,1763298000", "1763380800,1763384400,1763387600"))

        assertEquals(4000.0, weather.visibilityMeters!!, Tolerance)
        assertEquals(5, weather.precipitationProbabilityPercent)
    }

    @Test
    fun `leaves hourly-only values null when they are absent`() {
        val weather = parseCurrentWeather(ForecastPayload.replace("\"visibility\"", "\"ignored\""))

        assertNull(weather.visibilityMeters)
    }

    @Test
    fun `leaves hourly-only values null when the entry is null`() {
        val weather = parseCurrentWeather(ForecastPayload.replace("20,25", "null,25"))

        assertNull(weather.precipitationProbabilityPercent)
    }

    @Test
    fun `reports the reason behind a rejected request`() {
        val payload = """{"error":true,"reason":"Latitude must be in range of -90 to 90"}"""

        val failure = assertThrowsOpenMeteo { parseCurrentWeather(payload) }

        assertEquals("Latitude must be in range of -90 to 90", failure.message)
    }

    @Test
    fun `rejects a payload without a current block`() {
        val failure = assertThrowsOpenMeteo { parseCurrentWeather("""{"latitude":50.06}""") }

        assertTrue(failure.message!!.contains("\"current\""))
    }

    @Test
    fun `rejects a payload missing a required variable`() {
        val failure = assertThrowsOpenMeteo {
            parseCurrentWeather(ForecastPayload.replace("\"temperature_2m\": 21.3,", ""))
        }

        assertTrue(failure.message!!.contains("temperature_2m"))
    }

    @Test
    fun `rejects a body that is not json`() {
        assertThrowsOpenMeteo { parseCurrentWeather("<html>503</html>") }
    }

    private inline fun assertThrowsOpenMeteo(block: () -> Unit): OpenMeteoException = try {
        block()
        throw AssertionError("Expected an OpenMeteoException")
    } catch (e: OpenMeteoException) {
        e
    }

    private companion object {
        const val Tolerance = 1e-6

        val ForecastPayload = """
            {
              "latitude": 50.06,
              "longitude": 19.94,
              "utc_offset_seconds": 3600,
              "timezone": "Europe/Warsaw",
              "current": {
                "time": 1763294400,
                "interval": 900,
                "temperature_2m": 21.3,
                "apparent_temperature": 20.1,
                "relative_humidity_2m": 58,
                "is_day": 1,
                "weather_code": 2,
                "precipitation": 0.8,
                "pressure_msl": 1013.4,
                "wind_speed_10m": 18.2,
                "wind_direction_10m": 310,
                "wind_gusts_10m": 47.5
              },
              "hourly": {
                "time": [1763290800,1763294400,1763298000],
                "visibility": [4000.0,8000.0,12000.0],
                "precipitation_probability": [5,20,25]
              }
            }
        """.trimIndent()
    }
}
