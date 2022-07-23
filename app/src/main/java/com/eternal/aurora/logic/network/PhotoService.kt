package com.eternal.aurora.logic.network

import com.eternal.aurora.logic.model.Photo
import com.eternal.aurora.logic.model.PhotoDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PhotoService {
    @GET("photos")
    fun getPhotos() : Call<List<Photo>?>

    @GET("photos/{id}")
    fun getPhotoDetail(@Path("id") id: String): Call<PhotoDetail?>

}