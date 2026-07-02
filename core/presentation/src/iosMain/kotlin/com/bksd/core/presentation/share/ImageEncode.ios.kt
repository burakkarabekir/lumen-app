package com.bksd.core.presentation.share

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image

actual fun ImageBitmap.encodeToPngBytes(): ByteArray {
    val skiaBitmap = this.asSkiaBitmap()
    val image = Image.makeFromBitmap(skiaBitmap)
    val data = image.encodeToData(EncodedImageFormat.PNG) ?: error("png encode failed")
    return data.bytes
}
