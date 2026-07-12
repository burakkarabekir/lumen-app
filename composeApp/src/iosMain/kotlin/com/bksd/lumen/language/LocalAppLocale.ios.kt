package com.bksd.lumen.language

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.preferredLanguages

actual object LocalAppLocale {
    private const val LANG_KEY = "AppleLanguages"
    private val default: String = (NSLocale.preferredLanguages.firstOrNull() as? String) ?: "en"
    private val localAppLocale = staticCompositionLocalOf { default }

    actual val current: String
        @Composable get() = localAppLocale.current

    @Composable
    actual infix fun provides(value: String?): ProvidedValue<*> {
        val resolved = value ?: default
        val defaults = NSUserDefaults.standardUserDefaults
        if (value == null) {
            defaults.removeObjectForKey(LANG_KEY)
        } else {
            defaults.setObject(listOf(resolved), LANG_KEY)
        }
        return localAppLocale.provides(resolved)
    }
}
