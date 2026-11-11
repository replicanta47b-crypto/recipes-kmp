package com.example.restapi

import android.app.Application
import com.example.restapi.di.initKoin
import org.koin.android.ext.koin.androidContext

class RestApiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin { androidContext(this@RestApiApplication) }
    }
}
