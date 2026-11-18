package com.github.gszp1.carassistant.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private const val DATABASE_NAME = "car-assistant.db"

@Database(
    entities = [GridAppEntity::class],
    version = CarAssistantDatabase.DATABASE_VERSION,
    exportSchema = true,
)
abstract class CarAssistantDatabase : RoomDatabase() {

    abstract fun gridAppDao(): GridAppDao

    companion object {
        const val DATABASE_VERSION = 1

        @Volatile
        private var instance: CarAssistantDatabase? = null

        fun get(context: Context): CarAssistantDatabase =
            instance ?: synchronized(this) {
                instance ?: build(context.applicationContext).also { instance = it }
            }

        private fun build(context: Context): CarAssistantDatabase =
            Room.databaseBuilder(context, CarAssistantDatabase::class.java, DATABASE_NAME).build()
    }
}
