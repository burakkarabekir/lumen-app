package com.bksd.lumen

import android.app.UiModeManager
import android.content.Context
import androidx.core.content.edit

class SplashThemePrefs(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun update(isLoggedIn: Boolean, themeMode: String) {
        prefs.edit {
            putBoolean(KEY_LOGGED_IN, isLoggedIn)
            putString(KEY_THEME, themeMode)
        }
    }

    fun nightMode(): Int {
        if (!prefs.getBoolean(KEY_LOGGED_IN, false)) return UiModeManager.MODE_NIGHT_AUTO
        return when (prefs.getString(KEY_THEME, THEME_SYSTEM)) {
            THEME_LIGHT -> UiModeManager.MODE_NIGHT_NO
            THEME_DARK -> UiModeManager.MODE_NIGHT_YES
            else -> UiModeManager.MODE_NIGHT_AUTO
        }
    }

    var lastApplied: Int
        get() = prefs.getInt(KEY_APPLIED, Int.MIN_VALUE)
        set(value) = prefs.edit { putInt(KEY_APPLIED, value) }

    private companion object {
        const val NAME = "lumen_splash"
        const val KEY_LOGGED_IN = "is_logged_in"
        const val KEY_THEME = "theme_mode"
        const val KEY_APPLIED = "applied_night"
        const val THEME_SYSTEM = "SYSTEM"
        const val THEME_LIGHT = "LIGHT"
        const val THEME_DARK = "DARK"
    }
}
