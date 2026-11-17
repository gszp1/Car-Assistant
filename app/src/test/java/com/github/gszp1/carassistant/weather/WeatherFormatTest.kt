package com.github.gszp1.carassistant.weather

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant
import java.time.ZoneId

class WeatherFormatTest {

    @Test
    fun `rounds scalar values to whole units`() {
        assertEquals("21°C", formatTemperature(21.3))
        assertEquals("-4°C", formatTemperature(-3.6))
        assertEquals("18 km/h", formatSpeed(18.2))
        assertEquals("1013 hPa", formatPressure(1013.4))
        assertEquals("58%", formatPercent(58))
    }

    @Test
    fun `labels wind bearings with their sector`() {
        assertEquals("NW (310°)", formatBearing(WindDirection.NW, 310))
    }

    @Test
    fun `scales visibility to the most readable unit`() {
        assertEquals("800 m", formatVisibility(800.0))
        assertEquals("1.5 km", formatVisibility(1_500.0))
        assertEquals("8 km", formatVisibility(8_000.0))
        assertEquals("24 km", formatVisibility(24_000.0))
    }

    @Test
    fun `spells out dry conditions instead of a zero reading`() {
        assertEquals("None", formatPrecipitation(0.0))
        assertEquals("0.8 mm/h", formatPrecipitation(0.8))
        assertEquals("3 mm/h", formatPrecipitation(3.0))
    }

    @Test
    fun `falls back to coordinates when a place has no name`() {
        assertEquals("50.06, 19.94", formatCoordinates(GeoPoint(50.0614, 19.9383)))
    }

    @Test
    fun `renders the observation time in the requested zone`() {
        val observedAt = Instant.ofEpochSecond(1_763_294_400)

        assertEquals("13:00", formatObservedAt(observedAt, ZoneId.of("Europe/Warsaw")))
        assertEquals("12:00", formatObservedAt(observedAt, ZoneId.of("UTC")))
    }
}
