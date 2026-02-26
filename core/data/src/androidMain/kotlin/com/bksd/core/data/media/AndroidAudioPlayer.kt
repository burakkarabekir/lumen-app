package com.bksd.core.data.media

import android.media.MediaPlayer
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.MediaErrorType
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.domain.storage.AudioPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class AndroidAudioPlayer : AudioPlayer {

    private var mediaPlayer: MediaPlayer? = null
    private var progressJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _playbackState = MutableStateFlow(PlaybackState.STOPPED)
    override val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private val _currentPositionMs = MutableStateFlow(0L)
    override val currentPositionMs: StateFlow<Long> = _currentPositionMs.asStateFlow()

    private val _durationMs = MutableStateFlow(0L)
    override val durationMs: StateFlow<Long> = _durationMs.asStateFlow()

    private val _playbackAmplitudes = MutableStateFlow<List<Float>>(emptyList())
    override val playbackAmplitudes: StateFlow<List<Float>> = _playbackAmplitudes.asStateFlow()

    override suspend fun play(filePath: String): Result<Unit, AppError> {
        return try {
            // Release any previous player
            releaseInternal()

            mediaPlayer = MediaPlayer().apply {
                setDataSource(filePath)
                prepare()
                start()

                setOnCompletionListener {
                    _playbackState.value = PlaybackState.STOPPED
                    _currentPositionMs.value = 0L
                    progressJob?.cancel()
                }
            }

            _durationMs.value = mediaPlayer?.duration?.toLong() ?: 0L
            _playbackState.value = PlaybackState.PLAYING
            startProgressTracking()

            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            releaseInternal()
            Result.Error(AppError.Media(MediaErrorType.UNSUPPORTED_FORMAT))
        }
    }

    override suspend fun pause() {
        mediaPlayer?.pause()
        _playbackState.value = PlaybackState.PAUSED
        progressJob?.cancel()
    }

    override suspend fun resume() {
        mediaPlayer?.start()
        _playbackState.value = PlaybackState.PLAYING
        startProgressTracking()
    }

    override suspend fun stop() {
        mediaPlayer?.let {
            it.stop()
            it.prepare() // Reset to prepared state for potential replay
        }
        _playbackState.value = PlaybackState.STOPPED
        _currentPositionMs.value = 0L
        progressJob?.cancel()
    }

    override suspend fun seekTo(positionMs: Long) {
        mediaPlayer?.seekTo(positionMs.toInt())
        _currentPositionMs.value = positionMs
    }

    override fun release() {
        releaseInternal()
    }

    private fun releaseInternal() {
        progressJob?.cancel()
        try {
            mediaPlayer?.release()
        } catch (_: Exception) {
            // Ignore
        }
        mediaPlayer = null
        _playbackState.value = PlaybackState.STOPPED
        _currentPositionMs.value = 0L
        _durationMs.value = 0L
    }

    private fun startProgressTracking() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (isActive && _playbackState.value == PlaybackState.PLAYING) {
                val position = mediaPlayer?.currentPosition?.toLong() ?: 0L
                _currentPositionMs.value = position

                // Generate a simple amplitude-like progress indicator
                val duration = _durationMs.value
                if (duration > 0) {
                    val progress = position.toFloat() / duration.toFloat()
                    _playbackAmplitudes.value = generatePlaybackAmplitudes(progress)
                }

                delay(50) // 20fps update rate
            }
        }
    }

    /**
     * Generates a normalized progress indicator list for the visualizer.
     * In a production implementation, this would read actual audio data.
     */
    private fun generatePlaybackAmplitudes(progress: Float): List<Float> {
        val barCount = 40
        return List(barCount) { i ->
            val barProgress = i.toFloat() / barCount
            if (barProgress <= progress) 0.5f + (Math.random().toFloat() * 0.5f)
            else 0.1f
        }
    }
}
