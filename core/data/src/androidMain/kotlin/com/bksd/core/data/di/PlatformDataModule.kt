package com.bksd.core.data.di

import com.bksd.core.data.location.AndroidLocationProvider
import com.bksd.core.data.media.AndroidAudioPlayer
import com.bksd.core.data.media.AndroidVoiceRecorder
import com.bksd.core.domain.location.LocationProvider
import com.bksd.core.domain.storage.AudioPlayer
import com.bksd.core.domain.storage.VoiceRecorder
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformDataModule: Module = module {
    single<VoiceRecorder> { AndroidVoiceRecorder(androidContext()) }
    single<AudioPlayer> { AndroidAudioPlayer() }
    single<LocationProvider> { AndroidLocationProvider(androidContext()) }
}

