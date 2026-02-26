package com.bksd.core.data.media

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.MediaErrorType
import com.bksd.core.domain.error.Result
import com.bksd.core.domain.storage.RecordingStorage.Companion.RECORDING_FILE_EXTENSION
import com.bksd.core.domain.storage.RecordingStorage.Companion.TEMP_FILE_PREFIX
import com.bksd.core.domain.storage.VoiceRecorder
import kotlinx.cinterop.ExperimentalForeignApi
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
import platform.AVFAudio.AVAudioRecorder
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayAndRecord
import platform.AVFAudio.AVAudioSessionModeDefault
import platform.AVFAudio.AVAudioSessionRecordPermissionGranted
import platform.AVFAudio.AVFormatIDKey
import platform.AVFAudio.AVNumberOfChannelsKey
import platform.AVFAudio.AVSampleRateKey
import platform.AVFAudio.setActive
import platform.CoreAudioTypes.kAudioFormatMPEG4AAC
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUUID
import platform.Foundation.NSUserDomainMask
import platform.Foundation.timeIntervalSince1970

@OptIn(ExperimentalForeignApi::class)
class IosVoiceRecorder : VoiceRecorder {

    private var recorder: AVAudioRecorder? = null
    private var tempFileUrl: NSURL? = null

    private val _isRecording = MutableStateFlow(false)
    override val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _amplitudes = MutableStateFlow<List<Float>>(emptyList())
    override val amplitudes: StateFlow<List<Float>> = _amplitudes.asStateFlow()

    private val _elapsedMs = MutableStateFlow(0L)
    override val elapsedMs: StateFlow<Long> = _elapsedMs.asStateFlow()

    private var timerJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun hasPermission(): Boolean {
        return AVAudioSession.sharedInstance()
            .recordPermission() == AVAudioSessionRecordPermissionGranted
    }

    override suspend fun startRecording(): Result<Unit, AppError> {
        if (!hasPermission()) {
            return Result.Error(AppError.Media(MediaErrorType.PERMISSION_DENIED))
        }

        return try {
            val session = AVAudioSession.sharedInstance()
            session.setCategory(
                AVAudioSessionCategoryPlayAndRecord,
                //mode = AVAudioSessionModeDefault,
                error = null
            )
            session.setActive(true, null)

            val fileName =
                "${TEMP_FILE_PREFIX}_${NSUUID.UUID().UUIDString}.$RECORDING_FILE_EXTENSION"
            val documentDirectory = NSFileManager.defaultManager.URLsForDirectory(
                NSDocumentDirectory,
                inDomains = NSUserDomainMask
            ).first() as NSURL
            tempFileUrl = documentDirectory.URLByAppendingPathComponent(fileName)

            val settings = mapOf<Any?, Any>(
                AVFormatIDKey to kAudioFormatMPEG4AAC,
                AVSampleRateKey to 44100.0,
                AVNumberOfChannelsKey to 1
            )

            recorder = tempFileUrl?.let { AVAudioRecorder(it, settings, null) }
            recorder?.prepareToRecord()
            recorder?.meteringEnabled = true
            recorder?.record()

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
            recorder = null

            val session = AVAudioSession.sharedInstance()
            session.setActive(false, null)

            timerJob?.cancel()
            _isRecording.value = false

            val path = tempFileUrl?.path
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
        val path = tempFileUrl?.path
        if (path != null) {
            NSFileManager.defaultManager.removeItemAtPath(path, null)
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = scope.launch {
            val startTime = platform.Foundation.NSDate().timeIntervalSince1970
            while (isActive && _isRecording.value) {
                val currentTime = platform.Foundation.NSDate().timeIntervalSince1970
                _elapsedMs.value = ((currentTime - startTime) * 1000).toLong()

                recorder?.updateMeters()
                // iOS power is in decibels from -160 to 0
                val power = recorder?.averagePowerForChannel(0u) ?: -160f
                // normalize roughly to 0..1
                val normalizedAmp = ((power + 160f) / 160f).coerceIn(0f, 1f)

                _amplitudes.update { current ->
                    val newList = current.toMutableList()
                    newList.add(normalizedAmp)
                    if (newList.size > 40) newList.takeLast(40) else newList
                }

                delay(100)
            }
        }
    }

    private fun cleanup() {
        recorder?.stop()
        recorder = null
        timerJob?.cancel()
        _isRecording.value = false
        _elapsedMs.value = 0L
    }
}
