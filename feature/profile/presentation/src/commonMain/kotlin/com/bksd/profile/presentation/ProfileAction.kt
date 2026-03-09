package com.bksd.profile.presentation

/**
 * User intents for the Profile screen.
 * Theme-related actions are NOT here — theme is controlled via [ThemeController].
 */
sealed interface ProfileAction {
    data object OnSignOutClick : ProfileAction
    data object OnUpgradeClick : ProfileAction
    data object OnPrivacyClick : ProfileAction
    data object OnDataExportClick : ProfileAction
    data object OnThemeClick : ProfileAction
    data object OnNotificationsClick : ProfileAction
    data object OnEditAvatarClick : ProfileAction
    data object OnSettingsClick : ProfileAction
    data object OnBackClick : ProfileAction
}
