package com.github.gszp1.carassistant.weather

import org.junit.Assert.assertEquals
import org.junit.Test

class WindDirectionTest {

    @Test
    fun `labels each sector by its centre`() {
        assertEquals(WindDirection.N, WindDirection.fromDegrees(0.0))
        assertEquals(WindDirection.NE, WindDirection.fromDegrees(45.0))
        assertEquals(WindDirection.E, WindDirection.fromDegrees(90.0))
        assertEquals(WindDirection.S, WindDirection.fromDegrees(180.0))
        assertEquals(WindDirection.NW, WindDirection.fromDegrees(315.0))
    }

    @Test
    fun `rounds a bearing to the nearest compass point`() {
        assertEquals(WindDirection.WNW, WindDirection.fromDegrees(295.0))
        assertEquals(WindDirection.NW, WindDirection.fromDegrees(310.0))
        assertEquals(WindDirection.NNW, WindDirection.fromDegrees(330.0))
        assertEquals(WindDirection.N, WindDirection.fromDegrees(354.0))
    }

    @Test
    fun `wraps bearings outside a single turn`() {
        assertEquals(WindDirection.N, WindDirection.fromDegrees(360.0))
        assertEquals(WindDirection.W, WindDirection.fromDegrees(-90.0))
    }
}
