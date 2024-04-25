package com.test.practical.worker

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.test.practical.apiservices.RetrofitService
import com.test.practical.db.ItemsRepository
import com.test.practical.model.RespModel
import kotlinx.coroutines.Job
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyWork(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    val repoItem: ItemsRepository = ItemsRepository()


    override fun doWork(): Result {
        callApi()
        return Result.success()
    }

    private fun callApi() {
        val call: Call<RespModel> = RetrofitService.getInstance().getItems()
        call.enqueue(object : Callback<RespModel> {
            @SuppressLint("LongLogTag")
            override fun onResponse(
                call: Call<RespModel>,
                response: Response<RespModel>
            ) {
                if (response.body() != null) {
                    repoItem.deleteAll()
                    repoItem.insertData(response.body()!!.items)
                }
            }

            @SuppressLint("LongLogTag")
            override fun onFailure(call: Call<RespModel>, t: Throwable) {
            }
        })
    }


}