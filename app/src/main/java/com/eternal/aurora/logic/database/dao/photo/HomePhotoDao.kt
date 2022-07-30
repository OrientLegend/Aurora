package com.eternal.aurora.logic.database.dao.photo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.eternal.aurora.logic.database.entity.HomePhotoId
import com.eternal.aurora.logic.model.Photo
import kotlinx.coroutines.flow.Flow


@Dao
interface HomePhotoDao {

    @Insert
    suspend fun insert(photoId: HomePhotoId)

    @Update
    suspend fun update(photoId: HomePhotoId): Int

    @Query("DELETE FROM HomePhotoId WHERE photoId = :photoId")
    suspend fun deleteByPhotoId(photoId: String): Int

    @Query("SELECT * FROM Photo WHERE id IN (SELECT photoId FROM HomePhotoId)")
    fun loadAllHomePhotos(): Flow<List<Photo>>

}