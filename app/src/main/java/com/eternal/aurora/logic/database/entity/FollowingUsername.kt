package com.eternal.aurora.logic.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FollowingUsername(@PrimaryKey val username: String)
