package com.bksd.core.domain.theme

import kotlinx.coroutines.flow.Flow

/**
 * Provider-agnostic contract for persisting and observing the app theme.
 */
interface ThemeRepository {
    fun observeTheme(): Flow<AppThemeMode>
    suspend fun setTheme(mode: AppThemeMode)
}
