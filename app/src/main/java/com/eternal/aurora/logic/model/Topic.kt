package com.eternal.aurora.logic.model

import com.google.gson.annotations.SerializedName

data class Topic(
    val id: String,
    val title: String,
    val description: String?,
    @SerializedName("cover_photo") val coverPhoto: Photo,
    @SerializedName("total_photos") val totalPhotos: Int
)