package com.bksd.core.domain.storage

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.model.PlaybackState
import kotlinx.coroutines.flow.StateFlow

/**
 * Platform-agnostic audio player contract.
 * Implementations live in core:data with platform-specific APIs
 * (Android MediaPlayer, iOS AVAudioPlayer).
 */
interface AudioPlayer {
    val playbackState: StateFlow<PlaybackState>
    val currentPositionMs: StateFlow<Long>
    val durationMs: StateFlow<Long>
    val playbackAmplitudes: StateFlow<List<Float>>

    suspend fun play(filePath: String): Result<Unit, AppError>
    suspend fun pause()
    suspend fun resume()
    suspend fun stop()
    suspend fun seekTo(positionMs: Long)
    fun release()
}
