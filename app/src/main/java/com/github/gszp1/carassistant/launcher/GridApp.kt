package com.github.gszp1.carassistant.launcher

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.Flow

@Immutable
data class GridApp(
    val packageName: String,
    val label: String,
)

fun List<GridApp>.notPinnedIn(pinned: List<GridApp>): List<GridApp> {
    val pinnedPackages = pinned.mapTo(mutableSetOf(), GridApp::packageName)
    return filterNot { it.packageName in pinnedPackages }
}

interface GridAppStore {

    fun observeApps(): Flow<List<GridApp>>

    suspend fun add(app: GridApp)

    suspend fun remove(app: GridApp)
}
