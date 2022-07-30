package com.eternal.aurora.ui.utils

import androidx.room.Room
import com.eternal.aurora.App
import com.eternal.aurora.logic.database.AppDatabase
import com.eternal.aurora.logic.database.entity.FavoritePhotoId
import com.eternal.aurora.logic.database.entity.FollowingUsername
import com.eternal.aurora.logic.database.entity.HomePhotoId
import com.eternal.aurora.logic.model.Photo
import com.eternal.aurora.logic.model.UserDetail

object DatabaseUtil {

    private const val DATABASE_NAME = "app_database"

    private val db = Room.databaseBuilder(
        App.context,
        AppDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val photoDao = db.photoDao()
    private val homePhotoDao = db.homePhotoDao()
    private val favoritePhotoDao = db.favoritePhotoDao()
    private val userDao = db.userDao()
    private val followingUserDao = db.followingUserDao()


    //Home photos
    private suspend fun insertHomePhoto(photo: Photo) {
        homePhotoDao.insert(HomePhotoId(photo.id))
        insertOrUpdatePhoto(photo)
    }

    private suspend fun updateHomePhoto(photo: Photo) = homePhotoDao.update(HomePhotoId(photo.id))

    suspend fun insertOrUpdateHomePhoto(photo: Photo) {
        if (updateHomePhoto(photo) == 0) {
            insertHomePhoto(photo)
        }
    }

    suspend fun deleteHomePhotoById(id: String) {
        homePhotoDao.deleteByPhotoId(photoId = id)
        photoDao.deleteById(id = id)
    }

    fun loadAllHomePhoto() = homePhotoDao.loadAllHomePhotos()


    //Favorite photos
    private suspend fun insertFavoritePhoto(photo: Photo) {
        favoritePhotoDao.insert(FavoritePhotoId(photo.id))
        insertOrUpdatePhoto(photo)
    }

    private suspend fun updateFavoritePhoto(photo: Photo) =
        favoritePhotoDao.update(FavoritePhotoId(photo.id))

    suspend fun insertOrUpdateFavoritePhoto(photo: Photo) {
        if (updateFavoritePhoto(photo) == 0) {
            insertFavoritePhoto(photo)
        }
    }

    suspend fun deleteFavoritePhotoById(id: String) {
        favoritePhotoDao.deleteByPhotoId(photoId = id)
        photoDao.deleteById(id)
    }

    fun loadAllFavoritePhotos() = favoritePhotoDao.loadAllFavoritePhotos()

    fun loadAllFavoritePhotosId() = favoritePhotoDao.loadAll()


    //All photos
    private suspend fun insertPhoto(photo: Photo) = photoDao.insert(photo)

    private suspend fun updatePhoto(photo: Photo) = photoDao.update(photo)

    private suspend fun insertOrUpdatePhoto(photo: Photo) {
        if (updatePhoto(photo) == 0) {
            insertPhoto(photo)
        }
    }

    private suspend fun deletePhotoById(id: String) = photoDao.deleteById(id)

    private fun loadAllPhoto() = photoDao.loadAll()


    //Following user
    private suspend fun insertFollowingUser(userDetail: UserDetail) {
        followingUserDao.insert(FollowingUsername(userDetail.username))
        userDao.insert(userDetail)
    }

    private suspend fun updateFollowingUser(userDetail: UserDetail) =
        followingUserDao.update(FollowingUsername(userDetail.username))

    suspend fun insertOrUpdateFollowingUser(userDetail: UserDetail) {
        if (updateFollowingUser(userDetail) == 0) {
            insertFollowingUser(userDetail)
        }
    }

    suspend fun deleteFollowingUser(username: String) {
        followingUserDao.deleteByUsername(username)
        userDao.deleteByUsername(username)
    }

    fun loadAllFollowingUsername() = followingUserDao.loadAll()

    fun loadAllFollowingUser() = followingUserDao.loadAllFollowingUsers()

    //User
    private suspend fun insertUser(userDetail: UserDetail) = userDao.insert(userDetail)

    private suspend fun deleteUser(username: String) = userDao.deleteByUsername(username)
}