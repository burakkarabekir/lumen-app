package com.bksd.core.presentation.share

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

@Composable
actual fun rememberShareLauncher(): ShareLauncher {
    return remember {
        ShareLauncher(onShare = { text ->
            val controller = UIActivityViewController(
                activityItems = listOf(text),
                applicationActivities = null
            )
            UIApplication.sharedApplication.keyWindow?.rootViewController
                ?.presentViewController(controller, animated = true, completion = null)
        })
    }
}
