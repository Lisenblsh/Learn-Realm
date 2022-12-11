package com.lis.learnrealm

import com.lis.learnrealm.database.Cat
import com.lis.learnrealm.network.RetrofitService
import retrofit2.Response

class Repository(private val service: RetrofitService) {

    suspend fun getCatImage(limit: Int? = null):Response<List<Cat>>{
        return service.getCatImage(limit)
    }
}