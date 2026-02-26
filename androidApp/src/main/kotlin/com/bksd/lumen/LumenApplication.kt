package com.bksd.lumen

import android.app.Application
import com.bksd.lumen.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class LumenApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@LumenApplication)
            androidLogger()
        }
    }
}