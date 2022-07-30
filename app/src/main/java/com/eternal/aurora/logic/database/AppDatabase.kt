package com.eternal.aurora.logic.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eternal.aurora.logic.database.dao.photo.FavoritePhotoDao
import com.eternal.aurora.logic.database.dao.user.FollowingUserDao
import com.eternal.aurora.logic.database.dao.photo.HomePhotoDao
import com.eternal.aurora.logic.database.dao.photo.PhotoDao
import com.eternal.aurora.logic.database.dao.user.UserDao
import com.eternal.aurora.logic.database.entity.FavoritePhotoId
import com.eternal.aurora.logic.database.entity.FollowingUsername
import com.eternal.aurora.logic.database.entity.HomePhotoId
import com.eternal.aurora.logic.model.Photo
import com.eternal.aurora.logic.model.UserDetail


@Database(
    version = AppDatabase.DATABASE_VERSION,
    entities = [Photo::class, HomePhotoId::class, FavoritePhotoId::class, FollowingUsername::class, UserDetail::class],
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao

    abstract fun favoritePhotoDao(): FavoritePhotoDao

    abstract fun homePhotoDao(): HomePhotoDao

    abstract fun userDao(): UserDao

    abstract fun followingUserDao(): FollowingUserDao

    companion object {
        const val DATABASE_VERSION = 1
    }

}