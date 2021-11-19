package com.tanawatnunnak.coinsapplication.data.model


import com.google.gson.annotations.SerializedName

data class Stats(
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("offset")
    val offset: Int
)