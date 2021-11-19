package com.tanawatnunnak.coinsapplication.di

import com.tanawatnunnak.coinsapplication.ui.coin_list.adapter.CoinAdapter
import org.koin.dsl.module

val adapterModule = module {
    single { CoinAdapter() }
}