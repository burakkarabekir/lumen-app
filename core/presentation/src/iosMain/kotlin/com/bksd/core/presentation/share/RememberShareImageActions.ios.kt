package com.bksd.core.presentation.share

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSData
import platform.Foundation.create
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageWriteToSavedPhotosAlbum
import platform.UIKit.UIPasteboard
import platform.UIKit.popoverPresentationController

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private fun ByteArray.toNSData(): NSData {
    if (size == 0) return NSData()
    return usePinned {
        NSData.create(bytes = it.addressOf(0), length = size.toULong())
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberShareImageActions(): ShareImageActions {
    return remember {
        ShareImageActions(
            onShare = { png ->
                val image = UIImage(data = png.toNSData())
                val controller = UIActivityViewController(
                    activityItems = listOf(image),
                    applicationActivities = null
                )
                val root = UIApplication.sharedApplication.keyWindow?.rootViewController
                root?.view?.let { rootView ->
                    controller.popoverPresentationController?.sourceView = rootView
                    controller.popoverPresentationController?.sourceRect =
                        rootView.bounds.useContents {
                            CGRectMake(size.width / 2, size.height / 2, 0.0, 0.0)
                        }
                }
                root?.presentViewController(controller, animated = true, completion = null)
            },
            onSave = { png ->
                val image = UIImage(data = png.toNSData())
                UIImageWriteToSavedPhotosAlbum(image, null, null, null)
                true
            },
            onCopy = { png ->
                val image = UIImage(data = png.toNSData())
                UIPasteboard.generalPasteboard.setImage(image)
                true
            },
        )
    }
}
