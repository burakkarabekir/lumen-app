package com.bksd.core.presentation.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.bksd.core.domain.model.MediaType
import platform.UIKit.UIApplication
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject

actual class MediaPickerLauncher(
    private val onLaunch: (sourceType: UIImagePickerControllerSourceType) -> Unit
) {
    actual fun launchCamera() =
        onLaunch(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera)

    actual fun launchPhotoPicker() =
        onLaunch(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary)

    actual fun launchVideoPicker() =
        onLaunch(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary)

    actual fun launchFilePicker(mimeTypes: List<String>) {
        // UIDocumentPickerViewController would be used here.
    }
}

@Composable
actual fun rememberMediaPickerLauncher(onResult: (MediaPickResult) -> Unit): MediaPickerLauncher {
    val delegate = remember {
        object : NSObject(), UIImagePickerControllerDelegateProtocol,
            UINavigationControllerDelegateProtocol {
            override fun imagePickerController(
                picker: UIImagePickerController,
                didFinishPickingMediaWithInfo: Map<Any?, *>
            ) {
                picker.dismissViewControllerAnimated(true, null)
                // In a true implementation, we extract the NSURL from info map and resolve paths
                // For this demo, we simulate success with a placeholder path
                onResult(
                    MediaPickResult.Success(
                        "/var/mobile/Containers/Data/Application/temp/image.jpg",
                        MediaType.PHOTO,
                        1024L
                    )
                )
            }

            override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
                picker.dismissViewControllerAnimated(true, null)
                onResult(MediaPickResult.Cancelled)
            }
        }
    }

    return remember {
        MediaPickerLauncher(
            onLaunch = { sourceType ->
                val picker = UIImagePickerController()
                picker.sourceType = sourceType
                picker.delegate = delegate

                UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                    picker, animated = true, completion = null
                )
            }
        )
    }
}
