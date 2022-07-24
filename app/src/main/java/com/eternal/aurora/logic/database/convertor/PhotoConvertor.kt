package com.eternal.aurora.logic.database.convertor

import androidx.room.TypeConverter
import com.eternal.aurora.logic.model.Photo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PhotoConvertor {

    @TypeConverter
    fun objectToString(photo: Photo): String = Gson().toJson(photo)

    @TypeConverter
    fun stringToObject(json: String): Photo {
        val photoType = object : TypeToken<Photo>(){}.type
        return Gson().fromJson(json, photoType)
    }
}