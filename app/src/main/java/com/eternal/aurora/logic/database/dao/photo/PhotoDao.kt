package com.eternal.aurora.logic.database.dao.photo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.eternal.aurora.logic.model.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Insert
    suspend fun insert(photo: Photo)

    @Update
    suspend fun update(photo: Photo): Int

    @Query("SELECT * FROM Photo")
    fun loadAll(): Flow<List<Photo>>

    @Query("DELETE FROM Photo WHERE id = :id")
    suspend fun deleteById(id: String): Int

}