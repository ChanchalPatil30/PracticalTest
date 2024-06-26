package com.test.practical.model

import com.google.gson.annotations.SerializedName

data class RespModel(

    @SerializedName("total_count") var totalCount: Int? = null,
    @SerializedName("incomplete_results") var incompleteResults: Boolean? = null,
    @SerializedName("items") var items: ArrayList<Items> = arrayListOf()

)