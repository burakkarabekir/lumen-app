package com.bksd.core.data.di

import com.bksd.core.data.appinfo.IosAppInfoProvider
import com.bksd.core.data.connectivity.IosNetworkMonitor
import com.bksd.core.data.location.IosLocationProvider
import com.bksd.core.data.media.IosAudioPlayer
import com.bksd.core.data.media.IosVoiceRecorder
import com.bksd.core.data.notification.IosReminderScheduler
import com.bksd.core.data.storage.PlatformFileStorage
import com.bksd.core.data.storage.createPlatformDataStore
import com.bksd.core.domain.appinfo.AppInfoProvider
import com.bksd.core.domain.connectivity.NetworkMonitor
import com.bksd.core.domain.location.LocationProvider
import com.bksd.core.domain.notification.ReminderScheduler
import com.bksd.core.domain.storage.AudioPlayer
import com.bksd.core.domain.storage.VoiceRecorder
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformDataModule: Module = module {
    factory<VoiceRecorder> { IosVoiceRecorder() }
    factory<AudioPlayer> { IosAudioPlayer() }
    single<LocationProvider> { IosLocationProvider() }
    single { createPlatformDataStore() }
    single { PlatformFileStorage() }
    single<ReminderScheduler> { IosReminderScheduler(get()) }
    single<AppInfoProvider> { IosAppInfoProvider() }
    single<NetworkMonitor> { IosNetworkMonitor() }
}
