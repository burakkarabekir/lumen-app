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
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.Foundation.writeToFile
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.popoverPresentationController

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberPdfShareAction(): PdfShareAction {
    return remember {
        PdfShareAction { pdf, fileName ->
            val path = NSTemporaryDirectory() + fileName
            pdf.toNSData().writeToFile(path, atomically = true)
            val url = NSURL.fileURLWithPath(path)
            val controller = UIActivityViewController(
                activityItems = listOf(url),
                applicationActivities = null,
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
        }
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private fun ByteArray.toNSData(): NSData {
    if (isEmpty()) return NSData()
    return usePinned {
        NSData.create(bytes = it.addressOf(0), length = size.toULong())
    }
}
