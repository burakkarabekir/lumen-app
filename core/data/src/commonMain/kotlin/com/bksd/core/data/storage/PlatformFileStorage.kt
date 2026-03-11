package com.bksd.core.data.storage

expect class PlatformFileStorage {
    suspend fun saveImage(bytes: ByteArray, fileName: String): String
}
