package com.test.practical.apiservices

import com.test.practical.model.RespModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitService {

    @GET("search/repositories?q=%7Bquery%7D&sort=stars")
    suspend fun getAllItem() : Response<RespModel>


    @GET("search/repositories?q=%7Bquery%7D&sort=stars")
    fun getItems(): Call<RespModel>

    companion object {
        var retrofitService: RetrofitService? = null
        fun getInstance() : RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }

    }
}