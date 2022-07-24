package com.eternal.aurora.logic.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eternal.aurora.logic.database.dao.PhotoDao
import com.eternal.aurora.logic.database.entity.PhotoData
import com.eternal.aurora.logic.model.Photo


@Database(
    version = AppDatabase.DATABASE_VERSION,
    entities = [Photo::class],
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun photoDao(): PhotoDao

    companion object {
        const val DATABASE_VERSION = 1
    }

}