package com.eternal.aurora.logic.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eternal.aurora.logic.database.dao.FavoritePhotoDao
import com.eternal.aurora.logic.database.dao.FollowingUserDao
import com.eternal.aurora.logic.database.dao.HomePhotoDao
import com.eternal.aurora.logic.database.dao.PhotoDao
import com.eternal.aurora.logic.database.entity.FavoritePhotoId
import com.eternal.aurora.logic.database.entity.HomePhotoId
import com.eternal.aurora.logic.model.Photo


@Database(
    version = AppDatabase.DATABASE_VERSION,
    entities = [Photo::class, HomePhotoId::class, FavoritePhotoId::class],
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {

    abstract fun photoDao(): PhotoDao

    abstract fun favoritePhotoDao(): FavoritePhotoDao

    abstract fun homePhotoDao(): HomePhotoDao

    abstract fun followingUserDao(): FollowingUserDao

    companion object {
        const val DATABASE_VERSION = 1
    }

}