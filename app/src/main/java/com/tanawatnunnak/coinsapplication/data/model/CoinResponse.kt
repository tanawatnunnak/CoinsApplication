package com.tanawatnunnak.coinsapplication.data.model


import com.google.gson.annotations.SerializedName

data class CoinResponse(
    @SerializedName("data")
    val data: Data,
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String
)

data class Data(
    @SerializedName("coins")
    val coins: List<Coin?>?,
    @SerializedName("stats")
    val stats: Stats
)

data class Coin(
    @SerializedName("description")
    val description: String?,
    @SerializedName("iconUrl")
    val iconUrl: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("symbol")
    val symbol: String,
)
