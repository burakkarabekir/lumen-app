package com.bksd.lumen

import android.app.Application
import android.content.pm.ApplicationInfo
import com.bksd.lumen.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class LumenApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val isDebug = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        initKoin(isDebug = isDebug) {
            androidContext(this@LumenApplication)
            androidLogger(if (isDebug) Level.INFO else Level.NONE)
        }
    }
}
