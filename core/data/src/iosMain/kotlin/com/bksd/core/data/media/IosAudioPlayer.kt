package com.bksd.core.data.media

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.MediaErrorType
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.model.PlaybackState
import com.bksd.core.domain.storage.AudioPlayer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import platform.AVFAudio.AVAudioPlayer
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.setActive
import platform.Foundation.NSData
import platform.Foundation.dataWithContentsOfFile

@OptIn(ExperimentalForeignApi::class)
class IosAudioPlayer : AudioPlayer {

    private var audioPlayer: AVAudioPlayer? = null
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
            releaseInternal()

            val session = AVAudioSession.sharedInstance()
            session.setCategory(AVAudioSessionCategoryPlayback, error = null)
            session.setActive(true, null)

            val data = NSData.dataWithContentsOfFile(filePath)
                ?: return Result.Error(AppError.Media(MediaErrorType.UNSUPPORTED_FORMAT))

            audioPlayer = AVAudioPlayer(data, error = null).apply {
                prepareToPlay()
                meteringEnabled = true
                play()
            }

            _durationMs.value = ((audioPlayer?.duration ?: 0.0) * 1000).toLong()
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
        audioPlayer?.pause()
        _playbackState.value = PlaybackState.PAUSED
        progressJob?.cancel()
    }

    override suspend fun resume() {
        audioPlayer?.play()
        _playbackState.value = PlaybackState.PLAYING
        startProgressTracking()
    }

    override suspend fun stop() {
        audioPlayer?.stop()
        audioPlayer?.currentTime = 0.0
        _playbackState.value = PlaybackState.STOPPED
        _currentPositionMs.value = 0L
        progressJob?.cancel()
    }

    override suspend fun seekTo(positionMs: Long) {
        audioPlayer?.currentTime = positionMs.toDouble() / 1000.0
        _currentPositionMs.value = positionMs
    }

    override fun release() {
        releaseInternal()
    }

    private fun releaseInternal() {
        progressJob?.cancel()
        audioPlayer?.stop()
        audioPlayer = null

        try {
            AVAudioSession.sharedInstance().setActive(false, null)
        } catch (_: Exception) {
        }

        _playbackState.value = PlaybackState.STOPPED
        _currentPositionMs.value = 0L
        _durationMs.value = 0L
    }

    private fun startProgressTracking() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (isActive && _playbackState.value == PlaybackState.PLAYING) {
                val position = ((audioPlayer?.currentTime ?: 0.0) * 1000).toLong()
                _currentPositionMs.value = position

                // Check if playback completed
                val player = audioPlayer
                if (player != null && !player.isPlaying()) {
                    _playbackState.value = PlaybackState.STOPPED
                    _currentPositionMs.value = 0L
                    break
                }

                val duration = _durationMs.value
                if (duration > 0) {
                    val progress = position.toFloat() / duration.toFloat()
                    _playbackAmplitudes.value = generatePlaybackAmplitudes(progress)
                }

                delay(50)
            }
        }
    }

    private fun generatePlaybackAmplitudes(progress: Float): List<Float> {
        val barCount = 40
        return List(barCount) { i ->
            val barProgress = i.toFloat() / barCount
            if (barProgress <= progress) 0.5f + (kotlin.random.Random.nextFloat() * 0.5f)
            else 0.1f
        }
    }
}
