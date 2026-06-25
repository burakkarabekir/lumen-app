package com.bksd.core.data.media

import java.io.File

actual fun readFileBytes(localPath: String): ByteArray = File(localPath).readBytes()
