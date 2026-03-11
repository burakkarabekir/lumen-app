package com.bksd.profile.presentation

sealed interface ProfileAction {
    data object OnSignOutClick : ProfileAction
    data object OnUpgradeClick : ProfileAction
    data object OnPrivacyClick : ProfileAction
    data object OnDataExportClick : ProfileAction
    data object OnThemeClick : ProfileAction
    data object OnNotificationsClick : ProfileAction
    data object OnUploadPictureClick : ProfileAction
    data class OnPictureSelected(val bytes: ByteArray, val mimeType: String?) : ProfileAction
    data object OnSettingsClick : ProfileAction
}
