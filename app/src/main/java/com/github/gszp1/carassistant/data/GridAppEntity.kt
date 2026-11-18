package com.github.gszp1.carassistant.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.gszp1.carassistant.launcher.GridApp

@Entity(tableName = "grid_apps")
data class GridAppEntity(

    @PrimaryKey
    @ColumnInfo(name = "package_name")
    val packageName: String,

    @ColumnInfo(name = "label")
    val label: String,

    @ColumnInfo(name = "position")
    val position: Int,
) {
    fun toGridApp(): GridApp = GridApp(packageName = packageName, label = label)
}
