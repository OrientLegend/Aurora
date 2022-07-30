package com.eternal.aurora.logic.database.dao.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.eternal.aurora.logic.database.entity.FollowingUsername
import com.eternal.aurora.logic.model.UserDetail
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

    @Query("SELECT * FROM UserDetail WHERE username IN (SELECT * FROM FollowingUsername)")
    fun loadAllFollowingUsers(): Flow<List<UserDetail>>
}