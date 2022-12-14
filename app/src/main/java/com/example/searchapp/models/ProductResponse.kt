package com.example.searchapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductResponse(
    val id: String,
    val title: String,
    val price: Float,
    val condition: String,
    @SerializedName("thumbnail") val image: String
) : Parcelable
