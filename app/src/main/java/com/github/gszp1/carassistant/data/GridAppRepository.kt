package com.github.gszp1.carassistant.data

import android.content.Context
import com.github.gszp1.carassistant.launcher.GridApp
import com.github.gszp1.carassistant.launcher.GridAppStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GridAppRepository(private val dao: GridAppDao) : GridAppStore {

    override fun observeApps(): Flow<List<GridApp>> =
        dao.observeAll().map { apps -> apps.map(GridAppEntity::toGridApp) }

    override suspend fun add(app: GridApp) = dao.append(app.packageName, app.label)

    override suspend fun remove(app: GridApp) = dao.deleteByPackage(app.packageName)

    companion object {
        fun create(context: Context): GridAppRepository =
            GridAppRepository(CarAssistantDatabase.get(context).gridAppDao())
    }
}
