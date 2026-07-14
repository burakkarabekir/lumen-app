package com.bksd.core.data.language

import platform.Foundation.NSLocale
import platform.Foundation.preferredLanguages

actual fun deviceLanguageTag(): String =
    (NSLocale.preferredLanguages.firstOrNull() as? String)
        ?.take(2)
        ?.lowercase()
        ?: "en"
