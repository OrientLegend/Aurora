package com.eternal.aurora.logic.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.eternal.aurora.logic.database.convertor.PhotoConvertor
import com.eternal.aurora.logic.model.Photo

@Entity
@TypeConverters(PhotoConvertor::class)
data class PhotoData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val photo: Photo
)