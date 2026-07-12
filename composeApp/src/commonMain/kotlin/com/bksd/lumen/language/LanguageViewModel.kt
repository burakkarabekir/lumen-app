package com.bksd.lumen.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bksd.core.design_system.theme.LanguageController
import com.bksd.core.domain.language.AppLanguage
import com.bksd.core.domain.language.LanguageRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LanguageViewModel(
    private val languageRepository: LanguageRepository,
) : ViewModel(), LanguageController {

    override val language: StateFlow<AppLanguage> = languageRepository.observeLanguage()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppLanguage.SYSTEM,
        )

    override fun setLanguage(language: AppLanguage) {
        viewModelScope.launch {
            languageRepository.setLanguage(language)
        }
    }
}
