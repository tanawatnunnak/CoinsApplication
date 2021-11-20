package com.tanawatnunnak.coinsapplication.data.api

import com.tanawatnunnak.coinsapplication.data.constant.RouteApi
import com.tanawatnunnak.coinsapplication.data.model.CoinResponse
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinApi {

    @GET(RouteApi.COIN_LIST)
    fun getCoins(
        @Query("limit") limitPage: Int,
        @Query("offset") currentPage: Int
    ): Observable<Response<CoinResponse>>

    @GET(RouteApi.COIN_LIST)
    fun searchPrefix(
        @Query("prefix") prefix: String,
        @Query("limit") limitPage: Int,
        @Query("offset") currentPage: Int
    ): Flowable<Response<CoinResponse>>

    @GET(RouteApi.COIN_LIST)
    fun searchId(
        @Query("ids") id: Int,
        @Query("limit") limitPage: Int,
        @Query("offset") currentPage: Int
    ): Flowable<Response<CoinResponse>>
}