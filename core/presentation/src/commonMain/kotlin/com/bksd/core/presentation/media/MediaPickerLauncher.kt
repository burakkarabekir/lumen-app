package com.bksd.core.presentation.media

import androidx.compose.runtime.Composable

expect class MediaPickerLauncher {
    fun launchCamera()
    fun launchPhotoPicker()
    fun launchVideoPicker()
    fun launchFilePicker(mimeTypes: List<String> = emptyList())
}

@Composable
expect fun rememberMediaPickerLauncher(onResult: (MediaPickResult) -> Unit): MediaPickerLauncher
