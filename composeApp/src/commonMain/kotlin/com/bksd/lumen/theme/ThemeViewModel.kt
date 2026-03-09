package com.bksd.lumen.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bksd.core.design_system.theme.ThemeController
import com.bksd.core.domain.theme.AppThemeMode
import com.bksd.core.domain.theme.GetAppThemeUseCase
import com.bksd.core.domain.theme.SetAppThemeUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * App-level ViewModel that lives above navigation.
 * Implements [ThemeController] so it can be provided via [LocalThemeController].
 *
 * Responsibilities:
 * - Collect persisted theme preference from [GetAppThemeUseCase]
 * - Expose it as [StateFlow] for Compose collection
 * - Persist theme changes through [SetAppThemeUseCase]
 *
 * Does NOT resolve system dark/light mode — that happens in [rememberAppThemeState].
 */
class ThemeViewModel(
    getAppThemeUseCase: GetAppThemeUseCase,
    private val setAppThemeUseCase: SetAppThemeUseCase,
) : ViewModel(), ThemeController {

    override val themeMode: StateFlow<AppThemeMode> = getAppThemeUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppThemeMode.SYSTEM,
        )

    override fun setTheme(mode: AppThemeMode) {
        viewModelScope.launch {
            setAppThemeUseCase(mode)
        }
    }
}
