package com.eternal.aurora.logic.network

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

    suspend fun getPhotos() = fire {
        val response = photoService.getPhotos().await()
        if(response != null) {
            Result.success(response)
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