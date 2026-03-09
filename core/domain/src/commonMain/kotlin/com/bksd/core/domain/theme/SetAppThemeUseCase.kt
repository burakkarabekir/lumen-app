package com.bksd.core.domain.theme

/**
 * Persists the user's selected app theme mode.
 */
class SetAppThemeUseCase(
    private val repository: ThemeRepository
) {
    suspend operator fun invoke(mode: AppThemeMode) = repository.setTheme(mode)
}
