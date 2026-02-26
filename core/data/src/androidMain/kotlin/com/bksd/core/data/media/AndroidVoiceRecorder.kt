package com.bksd.core.data.media

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import androidx.core.content.ContextCompat
import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.MediaErrorType
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.storage.RecordingStorage.Companion.RECORDING_FILE_EXTENSION
import com.bksd.core.domain.storage.RecordingStorage.Companion.TEMP_FILE_PREFIX
import com.bksd.core.domain.storage.VoiceRecorder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

class AndroidVoiceRecorder(
    private val context: Context
) : VoiceRecorder {

    private var recorder: MediaRecorder? = null
    private var tempFile: File? = null

    private val _isRecording = MutableStateFlow(false)
    override val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _amplitudes = MutableStateFlow<List<Float>>(emptyList())
    override val amplitudes: StateFlow<List<Float>> = _amplitudes.asStateFlow()

    private val _elapsedMs = MutableStateFlow(0L)
    override val elapsedMs: StateFlow<Long> = _elapsedMs.asStateFlow()

    private var timerJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    override suspend fun startRecording(): Result<Unit, AppError> {
        if (!hasPermission()) {
            return Result.Error(AppError.Media(MediaErrorType.PERMISSION_DENIED))
        }

        return try {
            val fileName = "${TEMP_FILE_PREFIX}_${UUID.randomUUID()}.$RECORDING_FILE_EXTENSION"
            tempFile = File(context.cacheDir, fileName)

            recorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(tempFile?.absolutePath)
                prepare()
                start()
            }

            _isRecording.value = true
            _amplitudes.value = emptyList()
            _elapsedMs.value = 0L

            startTimer()
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            cleanup()
            Result.Error(AppError.Media(MediaErrorType.RECORDING_FAILED))
        }
    }

    override suspend fun stopRecording(): Result<String, AppError> {
        return try {
            recorder?.stop()
            recorder?.release()
            recorder = null

            timerJob?.cancel()
            _isRecording.value = false

            val path = tempFile?.absolutePath
            if (path != null) {
                Result.Success(path)
            } else {
                Result.Error(AppError.Media(MediaErrorType.RECORDING_FAILED))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            cleanup()
            Result.Error(AppError.Media(MediaErrorType.RECORDING_FAILED))
        }
    }

    override suspend fun cancelRecording() {
        cleanup()
        tempFile?.delete()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = scope.launch {
            val startTime = System.currentTimeMillis()
            while (isActive && _isRecording.value) {
                _elapsedMs.value = System.currentTimeMillis() - startTime

                // Read amplitude and normalize to 0..1 roughly
                val maxAmp = recorder?.maxAmplitude?.toFloat() ?: 0f
                val normalizedAmp = (maxAmp / 32767f).coerceIn(0f, 1f)

                _amplitudes.update { current ->
                    val newList = current.toMutableList()
                    newList.add(normalizedAmp)
                    // Keep last 40 samples for visualization
                    if (newList.size > 40) newList.takeLast(40) else newList
                }

                delay(100)
            }
        }
    }

    private fun cleanup() {
        try {
            recorder?.stop()
        } catch (e: Exception) {
            // Ignore if already stopped
        }
        recorder?.release()
        recorder = null
        timerJob?.cancel()
        _isRecording.value = false
        _elapsedMs.value = 0L
    }
}
