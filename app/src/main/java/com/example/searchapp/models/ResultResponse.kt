package com.example.searchapp.models

import com.google.gson.annotations.SerializedName

data class ResultResponse(
    @SerializedName("query") var query: String,
    @SerializedName("results") var products: List<ProductResponse>
)
