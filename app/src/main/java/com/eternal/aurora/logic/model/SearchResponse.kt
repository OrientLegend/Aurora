package com.eternal.aurora.logic.model

import com.google.gson.annotations.SerializedName

data class SearchPhotosResponse(val total: Int, val results: List<Photo>)

data class SearchCollectionsResponse(val total: Int, val results: List<PhotoCollection>)

data class SearchUsersResponse(val total: Int, val results: List<SearchUserInfo>)

data class SearchUserInfo(
    @SerializedName("id") val userId: String,
    val name: String,
    val username: String,
    val bio: String?,
    @SerializedName("profile_image") val profileImage: ProfileImage,
    @SerializedName("total_like") val totalLike: Int,
    @SerializedName("total_photos") val totalPhotos: Int,
    val photos: List<PhotoSample>
)

data class PhotoSample(val id: String, val urls: Urls)