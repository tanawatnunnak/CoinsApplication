package com.tanawatnunnak.coinsapplication.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class CoinApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(applicationContext)
            modules(listOf(networkModule, repositoryModule, viewModelModule, adapterModule))
        }
    }
}