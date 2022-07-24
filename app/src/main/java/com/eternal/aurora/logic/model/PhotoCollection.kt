package com.eternal.aurora.logic.model

import com.google.gson.annotations.SerializedName

data class PhotoCollection(
    val id: String,
    val title: String,
    val description: String?,
    @SerializedName("total_photos") val totalPhotos: Int,
    @SerializedName("private") val isPrivate: Boolean,
    val user: UserInfo,
    @SerializedName("cover_photo") val coverPhoto: Photo
)
