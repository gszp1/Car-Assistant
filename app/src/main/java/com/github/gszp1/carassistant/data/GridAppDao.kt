package com.github.gszp1.carassistant.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class GridAppDao {

    @Query("SELECT * FROM grid_apps ORDER BY position ASC")
    abstract fun observeAll(): Flow<List<GridAppEntity>>

    @Query("DELETE FROM grid_apps WHERE package_name = :packageName")
    abstract suspend fun deleteByPackage(packageName: String)

    @Transaction
    open suspend fun append(packageName: String, label: String) {
        insert(GridAppEntity(packageName = packageName, label = label, position = nextPosition()))
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract suspend fun insert(app: GridAppEntity)

    @Query("SELECT COALESCE(MAX(position), -1) + 1 FROM grid_apps")
    protected abstract suspend fun nextPosition(): Int
}
