package com.github.gszp1.carassistant.launcher

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Test

private val Maps = GridApp("com.google.android.apps.maps", "Maps")
private val Spotify = GridApp("com.spotify.music", "Spotify")
private val Radio = GridApp("com.example.radio", "Radio")

class GridAppsTest {

    private val scope = CoroutineScope(Dispatchers.Unconfined)
    private val store = FakeGridAppStore()

    private val grid = StoredGridApps(
        scope = scope,
        store = store,
        installedApps = { listOf(Maps, Spotify, Radio) },
    ).also { scope.launch { it.observePinned() } }

    @Test
    fun `publishes the stored apps`() {
        store.apps.value = listOf(Maps, Spotify)

        assertEquals(listOf(Maps, Spotify), grid.pinned)
    }

    @Test
    fun `adding an app stores it`() {
        grid.add(Spotify)

        assertEquals(listOf(Spotify), store.apps.value)
        assertEquals(listOf(Spotify), grid.pinned)
    }

    @Test
    fun `removing an app drops it from the store`() {
        store.apps.value = listOf(Maps, Spotify)

        grid.remove(Maps)

        assertEquals(listOf(Spotify), grid.pinned)
    }

    @Test
    fun `offers only the installed apps that are not pinned yet`() {
        store.apps.value = listOf(Spotify)

        grid.refreshAvailable()

        assertEquals(listOf(Maps, Radio), grid.available)
    }

    @Test
    fun `offers nothing until the installed apps are read`() {
        assertEquals(emptyList<GridApp>(), grid.available)
    }
}

private class FakeGridAppStore : GridAppStore {

    val apps = MutableStateFlow(emptyList<GridApp>())

    override fun observeApps(): Flow<List<GridApp>> = apps

    override suspend fun add(app: GridApp) {
        if (apps.value.none { it.packageName == app.packageName }) {
            apps.value = apps.value + app
        }
    }

    override suspend fun remove(app: GridApp) {
        apps.value = apps.value.filterNot { it.packageName == app.packageName }
    }
}
