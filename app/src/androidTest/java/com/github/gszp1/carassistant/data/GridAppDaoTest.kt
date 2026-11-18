package com.github.gszp1.carassistant.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GridAppDaoTest {

    private val database = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        CarAssistantDatabase::class.java,
    ).build()

    private val dao = database.gridAppDao()

    @After
    fun closeDatabase() = database.close()

    @Test
    fun keepsTheOrderInWhichAppsWereAdded() = runBlocking {
        dao.append("com.spotify.music", "Spotify")
        dao.append("com.google.android.apps.maps", "Maps")

        assertEquals(
            listOf("com.spotify.music", "com.google.android.apps.maps"),
            dao.observeAll().first().map(GridAppEntity::packageName),
        )
    }

    @Test
    fun ignoresAnAppThatIsAlreadyOnTheGrid() = runBlocking {
        dao.append("com.spotify.music", "Spotify")
        dao.append("com.google.android.apps.maps", "Maps")
        dao.append("com.spotify.music", "Spotify")

        assertEquals(2, dao.observeAll().first().size)
    }

    @Test
    fun removesAnAppByPackage() = runBlocking {
        dao.append("com.spotify.music", "Spotify")
        dao.append("com.google.android.apps.maps", "Maps")

        dao.deleteByPackage("com.spotify.music")

        assertEquals(
            listOf("com.google.android.apps.maps"),
            dao.observeAll().first().map(GridAppEntity::packageName),
        )
    }
}
