package com.bksd.core.presentation.share

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberShareLauncher(): ShareLauncher {
    val context = LocalContext.current
    return remember(context) {
        ShareLauncher(onShare = { text ->
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }
            context.startActivity(Intent.createChooser(sendIntent, null))
        })
    }
}
