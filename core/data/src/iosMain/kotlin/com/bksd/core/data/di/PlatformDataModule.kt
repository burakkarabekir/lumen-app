package com.bksd.core.data.di

import com.bksd.core.data.location.IosLocationProvider
import com.bksd.core.data.media.IosAudioPlayer
import com.bksd.core.data.media.IosVoiceRecorder
import com.bksd.core.domain.location.LocationProvider
import com.bksd.core.domain.storage.AudioPlayer
import com.bksd.core.domain.storage.VoiceRecorder
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformDataModule: Module = module {
    single<VoiceRecorder> { IosVoiceRecorder() }
    single<AudioPlayer> { IosAudioPlayer() }
    single<LocationProvider> { IosLocationProvider() }
}

