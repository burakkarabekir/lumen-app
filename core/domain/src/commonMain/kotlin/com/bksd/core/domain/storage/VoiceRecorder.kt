package com.bksd.core.domain.storage

import com.bksd.core.domain.error.AppError
import com.bksd.core.domain.error.Result
import kotlinx.coroutines.flow.StateFlow

interface VoiceRecorder {
    val isRecording: StateFlow<Boolean>
    val amplitudes: StateFlow<List<Float>>
    val elapsedMs: StateFlow<Long>

    suspend fun startRecording(): Result<Unit, AppError>
    suspend fun stopRecording(): Result<String, AppError>
    suspend fun cancelRecording()
    fun hasPermission(): Boolean
}
