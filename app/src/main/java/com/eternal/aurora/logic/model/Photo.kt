package com.eternal.aurora.logic.model

import android.os.Build
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Entity
data class Photo(
    @PrimaryKey val id: String,
    val width: Float,
    val height: Float,
    val color: String,
    @Embedded val urls: Urls,
    val likes: Int = 0,
    @Embedded val user: UserInfo
)


data class Urls(
    val raw: String = "",
    val regular: String = "",
    val full: String = "",
    val small: String = "",
    val thumb: String = ""
)

data class UserInfo(
    @SerializedName("id") val userId: String,
    val name: String,
    @SerializedName("portfolio_url") val portfolioUrl: String?,
    val bio: String? = "",
    @Embedded @SerializedName("profile_image") val profileImage: ProfileImage,
    @SerializedName("total_like") val totalLike: Int,
    @SerializedName("total_photos") val totalPhotos: Int
)

data class ProfileImage(
    @SerializedName("small") val smallScale: String = "",
    @SerializedName("medium") val mediumScale: String = "",
    @SerializedName("large") val largeScale: String = ""
)

fun Photo.encodeUrlsToUtf8() = this.copy(
    urls = this.urls.encodeUrlsToUtf8(),
    user = this.user.encodeUrlsToUtf8()
)

private fun Urls.encodeUrlsToUtf8(): Urls {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        return this.copy(
            raw = URLEncoder.encode(this.raw, StandardCharsets.UTF_8),
            regular = URLEncoder.encode(this.regular, StandardCharsets.UTF_8),
            full = URLEncoder.encode(this.full, StandardCharsets.UTF_8),
            small = URLEncoder.encode(this.small, StandardCharsets.UTF_8),
            thumb = URLEncoder.encode(this.thumb, StandardCharsets.UTF_8)
        )
    } else {
        return this.copy(
            raw = URLEncoder.encode(this.raw, "UTF-8"),
            regular = URLEncoder.encode(this.regular, "UTF-8"),
            full = URLEncoder.encode(this.full, "UTF-8"),
            small = URLEncoder.encode(this.small, "UTF-8"),
            thumb = URLEncoder.encode(this.thumb, "UTF-8"),
        )
    }
}

private fun ProfileImage.encodeUrlsToUtf8() =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.copy(
            smallScale = URLEncoder.encode(this.smallScale, StandardCharsets.UTF_8),
            mediumScale = URLEncoder.encode(this.mediumScale, StandardCharsets.UTF_8),
            largeScale = URLEncoder.encode(this.largeScale, StandardCharsets.UTF_8)
        )
    } else {
        this.copy(
            smallScale = URLEncoder.encode(this.smallScale, "UTF-8"),
            mediumScale = URLEncoder.encode(this.mediumScale, "UTF-8"),
            largeScale = URLEncoder.encode(this.largeScale, "UTF-8")
        )
    }

private fun UserInfo.encodeUrlsToUtf8() =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.copy(
            bio = URLEncoder.encode(this.bio ?: "", StandardCharsets.UTF_8),
            portfolioUrl = URLEncoder.encode(this.portfolioUrl ?: "", StandardCharsets.UTF_8),
            profileImage = profileImage.encodeUrlsToUtf8()
        )
    } else {
        this.copy(
            bio = URLEncoder.encode(this.bio ?: "", "UTF-8"),
            portfolioUrl = URLEncoder.encode(this.portfolioUrl ?: "", "UTF-8"),
            profileImage = profileImage.encodeUrlsToUtf8()
        )
    }

