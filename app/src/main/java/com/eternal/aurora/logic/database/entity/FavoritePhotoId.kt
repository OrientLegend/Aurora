package com.eternal.aurora.logic.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class FavoritePhotoId(
    @PrimaryKey
    val photoId: String
)
