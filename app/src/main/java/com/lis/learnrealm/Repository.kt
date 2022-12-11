package com.lis.learnrealm

import com.lis.learnrealm.database.CatImage
import com.lis.learnrealm.network.RetrofitService
import retrofit2.Response

class Repository(private val service: RetrofitService) {

    suspend fun getCatImage(limit: Int? = null):Response<List<CatImage>>{
        return service.getCatImage(limit)
    }
}