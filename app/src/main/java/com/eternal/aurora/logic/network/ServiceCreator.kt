package com.eternal.aurora.logic.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {

    private const val ACCESS_KEY = Key.ACCESS_KEY //You can get your key on https://unsplash.com/

    private const val BASE_URL = "https://api.unsplash.com"

    private val client = OkHttpClient.Builder().addInterceptor { chain: Interceptor.Chain ->
        val request = chain.request()
        val build = request.newBuilder()
            .addHeader("Authorization", "Client-ID $ACCESS_KEY").build()
        return@addInterceptor chain.proceed(build)
    }.retryOnConnectionFailure(true).build()


    private val retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    fun<T> create(serviceClass: Class<T>) = retrofit.create(serviceClass)

    inline fun<reified T> create() = create(T::class.java)
}