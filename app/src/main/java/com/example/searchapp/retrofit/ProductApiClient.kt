package com.example.searchapp.retrofit

import com.example.searchapp.models.ResultResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApiClient {
    @GET("search")
    fun getProductsBySearch(@Query("q") product: String): Call<ResultResponse>
}
