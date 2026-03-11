package com.bksd.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bksd.core.data.storage.PlatformFileStorage
import com.bksd.core.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.random.Random

class ProfileRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val fileStorage: PlatformFileStorage
) : ProfileRepository {

    private val avatarUrlKey = stringPreferencesKey("profile_avatar")

    override fun observeAvatarUrl(): Flow<String?> {
        return dataStore.data.map { prefs ->
            prefs[avatarUrlKey]
        }
    }

    override suspend fun setAvatarUrl(url: String?) {
        dataStore.edit { prefs ->
            if (url == null) {
                prefs.remove(avatarUrlKey)
            } else {
                prefs[avatarUrlKey] = url
            }
        }
    }

    override suspend fun saveAvatarImage(bytes: ByteArray, mimeType: String?): String {
        val extension = when (mimeType) {
            "image/png" -> "png"
            "image/webp" -> "webp"
            else -> "jpg"
        }
        val uniqueSuffix = Random.nextLong(0, Long.MAX_VALUE)
        val fileName = "avatar_$uniqueSuffix.$extension"
        return fileStorage.saveImage(bytes, fileName)
    }
}

