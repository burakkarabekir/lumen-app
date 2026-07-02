package com.bksd.profile.presentation

sealed interface EditProfileAction {
    data class OnNameChange(val name: String) : EditProfileAction
    data object OnChangePhotoClick : EditProfileAction
    data class OnPictureSelected(val bytes: ByteArray, val mimeType: String?) : EditProfileAction
    data object OnSaveClick : EditProfileAction
}
