package com.lis.learnrealm.network

import com.lis.learnrealm.database.Cat
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("search?")
    suspend fun getCatImage(@Query("limit") limit: Int?): Response<List<Cat>>

    companion object {
        private const val BASE_URL = "https://api.thecatapi.com/v1/images/"

        fun create(): RetrofitService {
            val okHttpClient = OkHttpClient().newBuilder()
                .retryOnConnectionFailure(true)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RetrofitService::class.java)
        }
    }
}