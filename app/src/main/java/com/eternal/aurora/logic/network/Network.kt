package com.eternal.aurora.logic.network

import com.eternal.aurora.logic.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object Network {

    private val photoService = ServiceCreator.create<PhotoService>()

    private fun handlePhoto(photo: Photo) = photo.copy(color = photo.color.removePrefix("#"))
    suspend fun getPhotos() = fire {
        val response = photoService.getPhotos().await()
        if(response != null) {
            val result = response.map {
                handlePhoto(it)
            }
            Result.success(result)
        } else {
            Result.failure(RuntimeException("Something went wrong when getPhotos"))
        }
    }

    suspend fun getPhotoDetail(id: String) = fire {
        val response = photoService.getPhotoDetail(id).await()
        if(response != null) {
            Result.success(response)
        } else {
            Result.failure(RuntimeException("Something went wrong when getPhotoDetail"))
        }
    }

    suspend fun getPhotoCollections() = fire {
        val response = photoService.getPhotoCollections().await()
        if(response != null) {
            Result.success(response)
        } else {
            Result.failure(RuntimeException("Something went wrong when getPhotoCollections"))
        }
    }

    suspend fun getPhotosFromCollection(collectionId: String, page: Int = 1) = fire {
        val response = photoService.getPhotosFromCollection(collectionId, page = page).await()
        if(response != null){
            val result = response.map { handlePhoto(it) }
            Result.success(result)
        } else {
            Result.failure(RuntimeException("Something went wrong when getPhotosFromCollection"))
        }
    }

    suspend fun getTopics() = fire {
        val response = photoService.getTopics().await()
        if(response != null) {
            Result.success(response)
        } else {
            Result.failure(RuntimeException("Something went wrong when getTopics"))
        }
    }

    suspend fun getPhotosFromTopic(topicId: String, page: Int = 1) = fire {
        val response = photoService.getPhotosFromTopic(topicId, page = page).await()
        if (response != null) {
            val result = response.map { handlePhoto(it) }
            Result.success(result)
        } else {
            Result.failure(RuntimeException("Something went wrong when getPhotosFromTopic"))
        }
    }

    suspend fun getSearchPhotosResult(queryString: String) = fire {
        val response = photoService.getSearchPhotosResponse(queryString = queryString).await()
        if (response != null) {
            val result = response.results.map { handlePhoto(it) }
            Result.success(result)
        } else {
            Result.failure(RuntimeException("Something went wrong when getSearchPhotosResult"))
        }
    }

    suspend fun getSearchCollectionsResult(queryString: String) = fire {
        val response = photoService.getSearchCollectionsResponse(queryString = queryString).await()
        if (response != null) {
            Result.success(response.results)
        } else {
            Result.failure(RuntimeException("Something went wrong when getSearchPhotosResult"))
        }
    }

    suspend fun getSearchUsersResult(queryString: String) = fire {
        val response = photoService.getSearchUsersResponse(queryString).await()
        if (response != null) {
            Result.success(response.results)
        } else {
            Result.failure(RuntimeException("Something went wrong when getSearchUsersResult"))
        }
    }

    suspend fun getUserDetail(username: String) = fire {
        val response = photoService.getUserDetail(username = username).await()
        if (response != null) {
            Result.success(response)
        } else {
            Result.failure(RuntimeException("Something went wrong when getUserDetail"))
        }
    }

    suspend fun getUserPhotos(username: String, page: Int = 1) = fire {
        val response = photoService.getUsersPhotos(username, page).await()
        if (response != null) {
            val result = response.map { handlePhoto(it) }
            Result.success(result)
        } else {
            Result.failure(RuntimeException("Something went wrong when getUserPhotos"))
        }
    }

    suspend fun getUserCollections(username: String, page: Int = 1) = fire {
        val response = photoService.getUsersCollections(username, page).await()
        if (response != null) {
            Result.success(response)
        } else {
            Result.failure(RuntimeException("Something went wrong when getUserCollections"))
        }
    }

    suspend fun getUserLikes(username: String, page: Int = 1) = fire {
        val response = photoService.getUsersLikes(username, page).await()
        if (response != null) {
            Result.success(response.map { handlePhoto(it) })
        } else {
            Result.failure(RuntimeException("Something went wrong when getUserLikes"))
        }
    }

    private suspend fun<T> fire(block: suspend () -> Result<T>) = withContext(Dispatchers.IO) {
        val result = try {
            block()
        } catch (e: Exception) {
            Result.failure(e)
        }
        return@withContext result
    }


    private suspend fun<T> Call<T>.await() = suspendCoroutine<T> { continuation ->
        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                if(body != null) {
                    continuation.resume(body)
                } else {
                    continuation.resumeWithException(RuntimeException("The body of response is null"))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                continuation.resumeWithException(t)
            }
        })
    }

}