package com.github.gszp1.carassistant.weather

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.Instant

class WeatherStateTest {

    private val locations = FakeLocationSource()
    private val client = FakeOpenMeteoClient()
    private val places = FakePlaceNames()

    private val state = WeatherState(
        scope = CoroutineScope(Dispatchers.Unconfined),
        client = client,
        locations = locations,
        places = places,
    )

    @Test
    fun `publishes the forecast for the resolved place`() {
        places.name = "Krakow"

        state.refresh()

        assertEquals("Krakow", state.uiState.placeName)
        assertEquals(Weather, state.uiState.weather)
        assertFalse(state.uiState.isLoading)
        assertNull(state.uiState.errorMessage)
    }

    @Test
    fun `labels the place with its coordinates when geocoding finds nothing`() {
        places.name = null

        state.refresh()

        assertEquals("50.06, 19.94", state.uiState.placeName)
    }

    @Test
    fun `surfaces why the location is unavailable`() {
        locations.result = Result.failure(LocationUnavailableException("Turn on location services"))

        state.refresh()

        assertEquals("Turn on location services", state.uiState.errorMessage)
        assertNull(state.uiState.weather)
        assertFalse(state.uiState.isLoading)
    }

    @Test
    fun `keeps the last forecast when a refresh fails`() {
        state.refresh()
        client.result = Result.failure(OpenMeteoException("Open-Meteo responded with HTTP 429"))

        state.refresh()

        assertEquals(Weather, state.uiState.weather)
        assertEquals("Open-Meteo responded with HTTP 429", state.uiState.errorMessage)
    }

    @Test
    fun `clears a stale error once a refresh succeeds`() {
        client.result = Result.failure(OpenMeteoException("Open-Meteo responded with HTTP 429"))
        state.refresh()
        client.result = Result.success(Weather)

        state.refresh()

        assertNull(state.uiState.errorMessage)
    }

    @Test
    fun `falls back to a generic message for a nameless failure`() {
        locations.result = Result.failure(RuntimeException())

        state.refresh()

        assertEquals("Weather is unavailable", state.uiState.errorMessage)
    }

    private class FakeLocationSource : LocationSource {
        var result: Result<GeoPoint> = Result.success(Point)
        override suspend fun currentPoint(): Result<GeoPoint> = result
    }

    private class FakeOpenMeteoClient : OpenMeteoClient {
        var result: Result<CurrentWeather> = Result.success(Weather)
        override suspend fun currentWeather(point: GeoPoint): Result<CurrentWeather> = result
    }

    private class FakePlaceNames : PlaceNames {
        var name: String? = "Krakow"
        override suspend fun nameFor(point: GeoPoint): String? = name
    }

    private companion object {
        val Point = GeoPoint(50.0614, 19.9383)

        val Weather = CurrentWeather(
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
