package com.tanawatnunnak.coinsapplication.di

import com.tanawatnunnak.coinsapplication.ui.coin_list.CoinConverter
import com.tanawatnunnak.coinsapplication.ui.coin_list.CoinListViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { CoinConverter() }
    single { CoinListViewModel(get(), get()) }
}