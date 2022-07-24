package com.eternal.aurora.ui.utils

import androidx.room.Room
import com.eternal.aurora.App
import com.eternal.aurora.logic.database.AppDatabase
import com.eternal.aurora.logic.database.entity.PhotoData
import com.eternal.aurora.logic.model.Photo

object DatabaseUtil {

    private const val DATABASE_NAME = "app_database"

    private val db = Room.databaseBuilder(
        App.context,
        AppDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val photoDao = db.photoDao()

    fun loadAllPhoto() = photoDao.loadAll()

    private suspend fun insertPhoto(photo: Photo) = photoDao.insert(photo)

    private suspend fun updatePhoto(photo: Photo) = photoDao.update(photo)

    suspend fun insertOrUpdatePhoto(photo: Photo) {
        if(updatePhoto(photo) == 0) {
            insertPhoto(photo)
        }
    }

    suspend fun deletePhotoById(id: String) = photoDao.deleteById(id)

}