package com.eternal.aurora.logic.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.eternal.aurora.logic.database.entity.FollowingUsername
import kotlinx.coroutines.flow.Flow

@Dao
interface FollowingUserDao {

    @Insert
    suspend fun insert(followingUsername: FollowingUsername)

    @Update
    suspend fun update(followingUsername: FollowingUsername): Int

    @Query("DELETE FROM FollowingUsername WHERE username = :username")
    suspend fun deleteByUsername(username: String): Int

    @Query("SELECT * FROM FollowingUsername")
    fun loadAll(): Flow<List<FollowingUsername>>

}