package com.github.gszp1.carassistant.launcher

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.github.gszp1.carassistant.data.GridAppRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Stable
interface GridApps {

    val pinned: List<GridApp>

    val available: List<GridApp>

    fun add(app: GridApp)

    fun remove(app: GridApp)

    fun refreshAvailable()

    companion object {
        val Empty: GridApps = object : GridApps {
            override val pinned = emptyList<GridApp>()
            override val available = emptyList<GridApp>()
            override fun add(app: GridApp) = Unit
            override fun remove(app: GridApp) = Unit
            override fun refreshAvailable() = Unit
        }
    }
}

@Composable
fun rememberGridApps(): GridApps {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val grid = remember(context, scope) {
        StoredGridApps(
            scope = scope,
            store = GridAppRepository.create(context.applicationContext),
            installedApps = { withContext(Dispatchers.IO) { context.installedApps() } },
        )
    }
    LaunchedEffect(grid) { grid.observePinned() }
    return grid
}

@Stable
internal class StoredGridApps(

    private val scope: CoroutineScope,

    private val store: GridAppStore,

    private val installedApps: suspend () -> List<GridApp>,

) : GridApps {

    override var pinned: List<GridApp> by mutableStateOf(emptyList())
        private set

    private var installed: List<GridApp> by mutableStateOf(emptyList())

    override val available: List<GridApp> get() = installed.notPinnedIn(pinned)

    suspend fun observePinned() {
        store.observeApps().collect { apps -> pinned = apps }
    }

    override fun add(app: GridApp) {
        scope.launch { store.add(app) }
    }

    override fun remove(app: GridApp) {
        scope.launch { store.remove(app) }
    }

    override fun refreshAvailable() {
        scope.launch { installed = installedApps() }
    }
}
