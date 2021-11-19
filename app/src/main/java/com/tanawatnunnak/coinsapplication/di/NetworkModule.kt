package com.tanawatnunnak.coinsapplication.di

import com.tanawatnunnak.coinsapplication.data.api.CoinApi
import com.tanawatnunnak.coinsapplication.data.api.RetrofitProvider
import org.koin.dsl.module
import retrofit2.Retrofit

private const val BASE_URL = "https://api.coinranking.com/"
val networkModule = module {
    single { RetrofitProvider(BASE_URL).provide() }
    single<CoinApi> { get<Retrofit>().create(CoinApi::class.java) }
}