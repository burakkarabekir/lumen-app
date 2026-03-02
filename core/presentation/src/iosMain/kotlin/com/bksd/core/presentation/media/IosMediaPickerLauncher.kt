@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.bksd.core.presentation.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.bksd.core.domain.model.MediaType
import platform.UIKit.UIApplication
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerImageURL
import platform.UIKit.UIImagePickerControllerMediaType
import platform.UIKit.UIImagePickerControllerMediaURL
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.darwin.NSObject

// UTI string constants
private const val UTI_IMAGE = "public.image"
private const val UTI_MOVIE = "public.movie"

actual class MediaPickerLauncher(
    private val onLaunchPhoto: (sourceType: UIImagePickerControllerSourceType) -> Unit,
    private val onLaunchVideo: (sourceType: UIImagePickerControllerSourceType) -> Unit,
    private val onLaunchCamera: () -> Unit
) {
    actual fun launchCamera() = onLaunchCamera()

    actual fun launchPhotoPicker() =
        onLaunchPhoto(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary)

    actual fun launchVideoPicker() =
        onLaunchVideo(UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary)

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

                val mediaType = didFinishPickingMediaWithInfo[UIImagePickerControllerMediaType] as? String
                val isVideo = mediaType == UTI_MOVIE

                // Get the file URL
                val url = if (isVideo) {
                    didFinishPickingMediaWithInfo[UIImagePickerControllerMediaURL] as? NSURL
                } else {
                    didFinishPickingMediaWithInfo[UIImagePickerControllerImageURL] as? NSURL
                }

                val filePath = url?.path
                if (filePath != null) {
                    val attrs = NSFileManager.defaultManager.attributesOfItemAtPath(filePath, null)
                    val fileSize = (attrs?.get("NSFileSize") as? Number)?.toLong() ?: 0L

                    onResult(
                        MediaPickResult.Success(
                            filePath = filePath,
                            type = if (isVideo) MediaType.VIDEO else MediaType.PHOTO,
                            sizeBytes = fileSize
                        )
                    )
                } else {
                    onResult(MediaPickResult.Error("Failed to get file path from picker"))
                }
            }

            override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
                picker.dismissViewControllerAnimated(true, null)
                onResult(MediaPickResult.Cancelled)
            }
        }
    }

    return remember {
        MediaPickerLauncher(
            onLaunchPhoto = { sourceType ->
                val picker = UIImagePickerController()
                picker.sourceType = sourceType
                picker.mediaTypes = listOf(UTI_IMAGE)
                picker.delegate = delegate

                UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                    picker, animated = true, completion = null
                )
            },
            onLaunchVideo = { sourceType ->
                val picker = UIImagePickerController()
                picker.sourceType = sourceType
                picker.mediaTypes = listOf(UTI_MOVIE)
                picker.delegate = delegate

                UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                    picker, animated = true, completion = null
                )
            },
            onLaunchCamera = {
                val picker = UIImagePickerController()
                picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
                picker.mediaTypes = listOf(UTI_IMAGE)
                picker.delegate = delegate

                UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                    picker, animated = true, completion = null
                )
            }
        )
    }
}
