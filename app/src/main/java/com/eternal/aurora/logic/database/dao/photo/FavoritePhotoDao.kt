package com.eternal.aurora.logic.database.dao.photo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.eternal.aurora.logic.database.entity.FavoritePhotoId
import com.eternal.aurora.logic.model.Photo
import kotlinx.coroutines.flow.Flow


@Dao
interface FavoritePhotoDao {

    @Insert
    suspend fun insert(photoId: FavoritePhotoId)

    @Update
    suspend fun update(photoId: FavoritePhotoId): Int

    @Query("DELETE FROM FavoritePhotoId WHERE photoId = :photoId")
    suspend fun deleteByPhotoId(photoId: String): Int

    @Query("SELECT * FROM FavoritePhotoId")
    fun loadAll(): Flow<List<FavoritePhotoId>>

    @Query("SELECT * FROM Photo WHERE id IN (SELECT photoId FROM FavoritePhotoId)")
    fun loadAllFavoritePhotos(): Flow<List<Photo>>

}