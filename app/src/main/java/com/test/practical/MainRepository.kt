package com.test.practical

import com.test.practical.apiservices.RetrofitService
import com.test.practical.model.RespModel

class MainRepository constructor(private val retrofitService: RetrofitService)  {

    suspend fun getAllItems() : NetworkState<RespModel> {
        val response = retrofitService.getAllItem()
        return if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }
}