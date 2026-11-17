package com.github.gszp1.carassistant.weather

import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Locale

class OpenMeteoRequestTest {

    @Test
    fun `formats coordinates locale independently`() {
        val default = Locale.getDefault()
        Locale.setDefault(Locale.forLanguageTag("pl-PL"))
        try {
            val url = OpenMeteoRequest.forecastUrl(GeoPoint(latitude = 50.0614, longitude = 19.9366))

            assertTrue(url, url.contains("latitude=50.0614"))
            assertTrue(url, url.contains("longitude=19.9366"))
        } finally {
            Locale.setDefault(default)
        }
    }

    @Test
    fun `asks for the variables the parser reads`() {
        val url = OpenMeteoRequest.forecastUrl(GeoPoint(latitude = -33.8688, longitude = 151.2093))

        assertTrue(url, url.startsWith("https://api.open-meteo.com/v1/forecast?"))
        assertTrue(url, url.contains("latitude=-33.8688"))
        assertTrue(url, url.contains("timeformat=unixtime"))
        assertTrue(url, url.contains("current=temperature_2m,"))
        assertTrue(url, url.contains("wind_gusts_10m"))
        assertTrue(url, url.contains("hourly=visibility,precipitation_probability"))
    }
}
