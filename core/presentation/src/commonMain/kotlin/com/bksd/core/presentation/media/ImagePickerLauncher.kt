package com.bksd.core.presentation.media

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
