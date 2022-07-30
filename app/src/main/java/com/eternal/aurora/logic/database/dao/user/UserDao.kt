package com.eternal.aurora.logic.database.dao.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.eternal.aurora.logic.model.UserDetail

@Dao
interface UserDao {

    @Insert
    suspend fun insert(userDetail: UserDetail)

    @Update
    suspend fun update(userDetail: UserDetail): Int

    @Query("DELETE FROM UserDetail WHERE username = :username")
    suspend fun deleteByUsername(username: String): Int

}