package com.example.searchapp.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    // Function to instance retrofit
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.mercadolibre.com/sites/MLA/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
