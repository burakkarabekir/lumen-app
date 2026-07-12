package com.bksd.core.data.language

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bksd.core.domain.language.AppLanguage
import com.bksd.core.domain.language.LanguageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LanguageRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : LanguageRepository {

    private companion object {
        val LANGUAGE_KEY = stringPreferencesKey("app_language")
    }

    override fun observeLanguage(): Flow<AppLanguage> =
        dataStore.data.map { prefs ->
            when (prefs[LANGUAGE_KEY]) {
                AppLanguage.ENGLISH.name -> AppLanguage.ENGLISH
                AppLanguage.TURKISH.name -> AppLanguage.TURKISH
                AppLanguage.GERMAN.name -> AppLanguage.GERMAN
                AppLanguage.SPANISH.name -> AppLanguage.SPANISH
                AppLanguage.FRENCH.name -> AppLanguage.FRENCH
                else -> AppLanguage.SYSTEM
            }
        }

    override suspend fun setLanguage(language: AppLanguage) {
        dataStore.edit { prefs -> prefs[LANGUAGE_KEY] = language.name }
    }
}
