package com.eternal.aurora.logic.model

import com.google.gson.annotations.SerializedName

data class UserDetail(
    val id: String,
    val username: String,
    val name: String,
    val bio: String?,
    val location: String,
    @SerializedName("profile_image") val profileImage: ProfileImage,
    @SerializedName("total_photos") val totalPhotos: Int,
    @SerializedName("total_collections") val totalCollections: Int,
    @SerializedName("total_likes") val totalLikes: Int
)