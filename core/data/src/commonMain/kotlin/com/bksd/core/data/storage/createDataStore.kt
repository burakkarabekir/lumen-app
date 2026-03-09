package com.bksd.core.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

/**
 * Creates a platform-specific DataStore instance.
 * Android: uses applicationContext (provided via DI)
 * iOS: uses NSDocumentDirectory
 */
expect fun createPlatformDataStore(): DataStore<Preferences>

internal const val DATA_STORE_FILE_NAME = "lumen_prefs.preferences_pb"
