package com.eternal.aurora.logic.network

import com.eternal.aurora.logic.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotoService {
    @GET("photos")
    fun getPhotos(): Call<List<Photo>?>

    @GET("photos/{id}")
    fun getPhotoDetail(@Path("id") id: String): Call<PhotoDetail?>

    @GET("collections")
    fun getPhotoCollections(): Call<List<PhotoCollection>?>

    @GET("collections/{id}/photos?per_page=30")
    fun getPhotosFromCollection(
        @Path("id") collectionId: String,
        @Query("page") page: Int = 1
    ): Call<List<Photo>?>

    @GET("topics")
    fun getTopics(): Call<List<Topic>?>

    @GET("topics/{id}/photos?per_page=30")
    fun getPhotosFromTopic(
        @Path("id") topicId: String,
        @Query("page") page: Int = 1
    ): Call<List<Photo>?>

    @GET("search/photos")
    fun getSearchPhotosResponse(@Query("query") queryString: String): Call<SearchPhotosResponse?>

    @GET("search/collections")
    fun getSearchCollectionsResponse(@Query("query") queryString: String): Call<SearchCollectionsResponse?>

    @GET("search/users")
    fun getSearchUsersResponse(@Query("query") queryString: String): Call<SearchUsersResponse?>

    @GET("users/{username}")
    fun getUserDetail(@Path("username") username: String): Call<UserDetail?>

    @GET("users/{username}/photos?per_page=30")
    fun getUsersPhotos(
        @Path("username") username: String,
        @Query("page") page: Int = 1
    ): Call<List<Photo>?>

    @GET("users/{username}/collections?per_page=30")
    fun getUsersCollections(
        @Path("username") username: String,
        @Query("page") page: Int = 1
    ): Call<List<PhotoCollection>?>

    @GET("users/{username}/likes?per_page=30")
    fun getUsersLikes(
        @Path("username") username: String,
        @Query("page") page: Int = 1
    ): Call<List<Photo>?>

}