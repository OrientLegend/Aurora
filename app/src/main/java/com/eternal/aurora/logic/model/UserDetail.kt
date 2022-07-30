package com.eternal.aurora.logic.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class UserDetail(
    @PrimaryKey val id: String,
    val username: String,
    val name: String,
    val bio: String?,
    val location: String,
    @Embedded @SerializedName("profile_image") val profileImage: ProfileImage,
    @SerializedName("total_photos") val totalPhotos: Int,
    @SerializedName("total_collections") val totalCollections: Int,
    @SerializedName("total_likes") val totalLikes: Int
)