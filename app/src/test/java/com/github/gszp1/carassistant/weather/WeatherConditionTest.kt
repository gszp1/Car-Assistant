package com.github.gszp1.carassistant.weather

import com.github.gszp1.carassistant.ui.components.WeatherSymbol
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherConditionTest {

    @Test
    fun `maps known wmo codes`() {
        assertEquals(WeatherCondition.CLEAR, WeatherCondition.fromWmoCode(0))
        assertEquals(WeatherCondition.OVERCAST, WeatherCondition.fromWmoCode(3))
        assertEquals(WeatherCondition.FREEZING_DRIZZLE, WeatherCondition.fromWmoCode(57))
        assertEquals(WeatherCondition.HEAVY_RAIN, WeatherCondition.fromWmoCode(65))
        assertEquals(WeatherCondition.THUNDERSTORMS_HAIL, WeatherCondition.fromWmoCode(99))
    }

    @Test
    fun `falls back to unknown for unlisted codes`() {
        assertEquals(WeatherCondition.UNKNOWN, WeatherCondition.fromWmoCode(4))
        assertEquals(WeatherCondition.UNKNOWN, WeatherCondition.fromWmoCode(-1))
    }

    @Test
    fun `switches to the night icon only where one exists`() {
        assertEquals(WeatherSymbol.CLEAR_DAY, WeatherCondition.CLEAR.symbol(isDay = true))
        assertEquals(WeatherSymbol.CLEAR_NIGHT, WeatherCondition.CLEAR.symbol(isDay = false))
        assertEquals(WeatherSymbol.RAIN, WeatherCondition.RAIN.symbol(isDay = false))
    }
}
