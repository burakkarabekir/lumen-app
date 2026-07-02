package com.bksd.core.data.di

import com.bksd.core.data.appinfo.AndroidAppInfoProvider
import com.bksd.core.data.location.AndroidLocationProvider
import com.bksd.core.data.media.AndroidAudioPlayer
import com.bksd.core.data.media.AndroidVoiceRecorder
import com.bksd.core.data.notification.AndroidReminderScheduler
import com.bksd.core.data.storage.PlatformFileStorage
import com.bksd.core.data.storage.createDataStore
import com.bksd.core.domain.appinfo.AppInfoProvider
import com.bksd.core.domain.location.LocationProvider
import com.bksd.core.domain.notification.ReminderScheduler
import com.bksd.core.domain.storage.AudioPlayer
import com.bksd.core.domain.storage.VoiceRecorder
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformDataModule: Module = module {
    factory<VoiceRecorder> { AndroidVoiceRecorder(androidContext()) }
    factory<AudioPlayer> { AndroidAudioPlayer() }
    single<LocationProvider> { AndroidLocationProvider(androidContext()) }
    single { createDataStore(androidContext()) }
    single { PlatformFileStorage(androidContext()) }
    single<ReminderScheduler> { AndroidReminderScheduler(androidContext()) }
    single<AppInfoProvider> { AndroidAppInfoProvider(androidContext()) }
}
