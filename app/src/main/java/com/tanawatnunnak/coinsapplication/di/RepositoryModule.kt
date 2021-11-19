package com.tanawatnunnak.coinsapplication.di

import com.tanawatnunnak.coinsapplication.data.repository.CoinRepository
import com.tanawatnunnak.coinsapplication.data.repository.CoinRepositoryImp
import org.koin.dsl.module

val  repositoryModule = module {
    single<CoinRepository> { CoinRepositoryImp(get()) }
}