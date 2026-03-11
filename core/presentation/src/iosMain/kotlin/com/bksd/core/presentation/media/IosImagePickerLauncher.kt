@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class, kotlinx.cinterop.BetaInteropApi::class)

package com.bksd.core.presentation.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.UIKit.UIApplication
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerImageURL
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject
import platform.posix.memcpy

@Composable
actual fun rememberImagePickerLauncher(
    onResult: (PickedImageData) -> Unit
): ImagePickerLauncher {
    val delegate = remember {
        object : NSObject(), UIImagePickerControllerDelegateProtocol,
            UINavigationControllerDelegateProtocol {
            override fun imagePickerController(
                picker: UIImagePickerController,
                didFinishPickingMediaWithInfo: Map<Any?, *>
            ) {
                picker.dismissViewControllerAnimated(true, null)

                val url = didFinishPickingMediaWithInfo[UIImagePickerControllerImageURL] as? NSURL
                if (url != null) {
                    val nsData = NSData.dataWithContentsOfURL(url)
                    if (nsData != null) {
                        val bytes = nsData.toByteArray()
                        
                        // Infer a simple mimetype or leave it generic since NSURL doesn't provide it directly simply
                        val mimeType = if (url.pathExtension?.lowercase() == "png") "image/png" else "image/jpeg"
                        
                        onResult(PickedImageData(bytes, mimeType))
                    }
                }
            }

            override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
                picker.dismissViewControllerAnimated(true, null)
            }
        }
    }

    return remember {
        ImagePickerLauncher(
            onLaunch = {
                val picker = UIImagePickerController()
                picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
                picker.mediaTypes = listOf("public.image")
                picker.delegate = delegate

                UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                    picker, animated = true, completion = null
                )
            }
        )
    }
}

// Extension to convert NSData to ByteArray safely
@kotlinx.cinterop.ExperimentalForeignApi
private fun NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    if (size == 0) return ByteArray(0)
    val byteArray = ByteArray(size)
    byteArray.usePinned { pinned ->
        memcpy(pinned.addressOf(0), bytes, length)
    }
    return byteArray
}
