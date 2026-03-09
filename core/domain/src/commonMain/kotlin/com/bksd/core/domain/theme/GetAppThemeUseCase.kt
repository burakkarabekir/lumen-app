package com.bksd.core.domain.theme

import kotlinx.coroutines.flow.Flow

/**
 * Observes the current app theme mode from the repository.
 */
class GetAppThemeUseCase(
    private val repository: ThemeRepository
) {
    operator fun invoke(): Flow<AppThemeMode> = repository.observeTheme()
}
