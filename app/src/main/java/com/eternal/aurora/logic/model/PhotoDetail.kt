package com.eternal.aurora.logic.model

import com.google.gson.annotations.SerializedName

data class PhotoDetail(
    val id: String,
    val width: Int,
    val height: Int,
    val downloads: Int,
    val likes: Int,
    val description: String?,
    val exif: Exif,
    val location: PhotoLocation
)

data class Exif(
    val name: String?,
    @SerializedName("exposure_time") val exposureTime: String?,
    @SerializedName("focal_length") val focalLength: Float?,
    val iso: String?
)

data class PhotoLocation(val city: String?, val country: String?)

