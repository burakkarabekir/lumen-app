package com.bksd.core.presentation.media

import androidx.compose.runtime.Composable

@Composable
expect fun rememberImagePickerLauncher(
    onResult: (PickedImageData) -> Unit
): ImagePickerLauncher
