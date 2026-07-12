package com.bksd.core.design_system.theme

import com.bksd.core.domain.language.AppLanguage
import kotlinx.coroutines.flow.StateFlow

interface LanguageController {
    val language: StateFlow<AppLanguage>
    fun setLanguage(language: AppLanguage)
}
