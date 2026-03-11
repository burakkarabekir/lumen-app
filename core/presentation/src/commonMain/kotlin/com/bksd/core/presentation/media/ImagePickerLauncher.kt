package com.bksd.core.presentation.media

import androidx.compose.runtime.Composable

class PickedImageData(
    val bytes: ByteArray,
    val mimeType: String?
)

class ImagePickerLauncher(
    private val onLaunch: () -> Unit
) {
    fun launch() {
        onLaunch()
    }
}

@Composable
expect fun rememberImagePickerLauncher(
    onResult: (PickedImageData) -> Unit
): ImagePickerLauncher
